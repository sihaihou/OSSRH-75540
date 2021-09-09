package com.reyco.core.binlog.autoConfigure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.Header;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.reyco.core.binlog.commons.StringUtil;
import com.reyco.core.binlog.model.LogDefinition;
import com.reyco.core.binlog.properties.BinlogProperties;
import com.reyco.core.binlog.service.OperationLogService;

/**
 * 默认的实现
 * 
 * @author Reyco
 *
 */
@SuppressWarnings("all")
@EnableConfigurationProperties(BinlogProperties.class)
@ConditionalOnClass(value = { BinlogProperties.class })
public class ParseLogAutoConfigure extends AbstractParseBinLog implements InitializingBean,DisposableBean {

	private static Logger logger = LoggerFactory.getLogger(ParseLogAutoConfigure.class);

	public final static Integer batchSize = 1024;

	@Autowired
	private BinlogProperties binlogProperties;

	// binlog的状态 true:开启 false:关闭
	protected volatile Boolean open = false;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 注册FilterTablePostProcessor
		registrFilterTablePostProcessors();

		// 注册logPostProcessor
		registerLogPostProcessors();

		// 启动binlog
		run();
	}
	@Override
	public void destroy() throws Exception {
		close();
	}
	@Override
	public void parse() throws Exception {
		CanalConnector connector = null;
		try {
			// 获取连接
			connector = DefaultCanalConnector.getCanalConnector(binlogProperties.getServerConfig().getHost(),
					binlogProperties.getServerConfig().getPort());
			connector.connect();
			connector.subscribe(".*\\..*");
			connector.rollback();
			while (open) {
				// 获取指定数量的数据
				Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
				long batchId = message.getId();
				List<Entry> entries = message.getEntries();
				int size = entries.size();
				try {
					if (batchId == -1 || size == 0) { // 休息一秒
						notDataParsePostProcessor(message);
						Thread.sleep(1000);
					} else {
						logger.debug(String.format("message[ batchId = %s , size= %s ] \n", batchId, size));
						parseEntries(entries);
						connector.ack(batchId); // 提交确认
					}
				} catch (Exception e) {
					logger.debug("##################处理失败, 回滚数据:batchId=" + batchId);
					e.printStackTrace();
					connector.rollback(batchId); // 处理失败, 回滚数据
				}
			}
			logger.debug("##################normal stop and exit...");
		} finally {
			if (connector != null) {
				logger.debug("##################释放链接");
				connector.disconnect();
			}
		}
	}

	/**
	 * 没有数据
	 * 
	 * @param connector
	 * @param batchId
	 * @throws InterruptedException
	 */
	void notDataParsePostProcessor(Message message) {
		//
	}

	private void parseEntries(List<CanalEntry.Entry> entrys) throws Exception {
		for (Entry entry : entrys) {
			parseEntry(entry);
		}
	}

	@Override
	protected void parseEntry(Entry entry) throws Exception {
		// 事务开始或者事务结束的终端当前循环执行。。。
		if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
				|| entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
			return;
		}
		// 获取header
		Header header = entry.getHeader();
		// 库名
		String database = header.getSchemaName();
		// 表名
		String tableName = header.getTableName();

		// 变化数据
		CanalEntry.RowChange rowChage = getCanalEntryRowChange(entry);
		// 事件类型：insert、update、delete
		CanalEntry.EventType eventType = rowChage.getEventType();

		logger.debug(String.format("==========> binlog[%s:%s] , database:%s, tableName:%s,eventType:%s",
				header.getLogfileName(), header.getLogfileOffset(), database, tableName, eventType));

		// 执行库表过滤
		Boolean fiterTableFlag = invokeFilterTablePostProcessors(database, tableName);
		if (!fiterTableFlag) {
			return;
		}
		// 获取到data
		List<RowData> rowDatas = rowChage.getRowDatasList();
		// 处理data
		parseRowData(rowDatas, database, tableName, eventType);
	}

	/**
	 * 解析RowData
	 * 
	 * @param rowDatas
	 *            变的数据
	 * @param database
	 *            库名
	 * @param tableName
	 *            表名
	 * @param eventType
	 *            类型（update、insert、delete）
	 * @throws Exception
	 */
	@Override
	protected void parseRowData(List<RowData> rowDatas, String database, String tableName, EventType eventType)
			throws Exception {
		for (RowData rowData : rowDatas) {
			List<LogDefinition> logDefinitions = null;
			String primaryKey = binlogProperties.getPrimaryKey();
			if (eventType == CanalEntry.EventType.DELETE) {
				logger.debug("##################删除数据;database:" + database + ", tableName:" + tableName);
				logDefinitions = getLogDefinitionByDelete(rowData, database, tableName, primaryKey);
			} else if (eventType == CanalEntry.EventType.INSERT) {
				logger.debug("##################新增;database:" + database + ", tableName:" + tableName);
				String updateByColumn = binlogProperties.getUpdateByColumn();
				String updateTmColumn = binlogProperties.getUpdateTmColumn();
				logDefinitions = getLogDefinitionByInsert(rowData, database, tableName, primaryKey, updateByColumn,updateTmColumn);
			} else {
				logger.debug("##################更新;database:" + database + ", tableName:" + tableName);
				String updateByColumn = binlogProperties.getUpdateByColumn();
				String updateTmColumn = binlogProperties.getUpdateTmColumn();
				logDefinitions = getLogDefinitionByUpdate(rowData, database, tableName, primaryKey, updateByColumn,updateTmColumn);
			}
			if (logDefinitions != null && !logDefinitions.isEmpty()) {
				// Invoke log processors registered as beans in the context
				invokeLogPostProcessors(logDefinitions, tableName);
				
				// 拿到所有的实现类,循环处理
				Map<String, OperationLogService> operationLogServiceMap = applicationContext.getBeansOfType(OperationLogService.class);
				if (operationLogServiceMap != null) {
					//忽略的类
					String excludeOperationLogServiceNames = binlogProperties.getExcludeOperationLogServiceNames();
					String[] excludeOperationLogServiceNameArray = StringUtil.strToArray(excludeOperationLogServiceNames);
					//移除忽略的实现类
					removeEexcludeOperationLogService(operationLogServiceMap,excludeOperationLogServiceNameArray);
					//循环调用
					for (OperationLogService operationLogService : operationLogServiceMap.values()) {
						operationLogService.save(logDefinitions);
					}
				}
			}
		}
	}
	/**
	 * 移除忽略的实现类
	 * @param operationLogServiceMap
	 * @param excludeOperationLogServiceNameArray
	 */
	private void removeEexcludeOperationLogService(Map<String, OperationLogService> operationLogServiceMap,String[] excludeOperationLogServiceNameArray) {
		for (int i=0;i<excludeOperationLogServiceNameArray.length;i++) {
			operationLogServiceMap.remove(excludeOperationLogServiceNameArray[i]);
		}
	}

	/**
	 * 获取RowChange
	 * 
	 * @param entry
	 * @return
	 */
	private RowChange getCanalEntryRowChange(CanalEntry.Entry entry) {
		try {
			return CanalEntry.RowChange.parseFrom(entry.getStoreValue());
		} catch (Exception e) {
			throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
		}
	}

	public void run() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					open();
				} catch (Exception e) {
					logger.error("##################启动Binlog失败");
					e.printStackTrace();
				}
			}
		}, "binlogThread").start();
	}

	@Override
	public void close() throws Exception {
		logger.debug("##################关闭Binlog");
		open = false;
	}

	@Override
	public void open() throws Exception {
		if (!open) {
			synchronized (this) {
				if (!open) {
					logger.debug("##################启用Binlog");
					open = true;
					parse();
				}
			}
		}
	}

}