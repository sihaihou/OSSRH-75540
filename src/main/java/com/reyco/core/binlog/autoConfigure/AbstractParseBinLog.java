package com.reyco.core.binlog.autoConfigure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.reyco.core.binlog.controller.IndexController;
import com.reyco.core.binlog.model.LogDefinition;
import com.reyco.core.binlog.processor.FilterTablePostProcessor;
import com.reyco.core.binlog.processor.LogPostProcessor;
import com.reyco.core.binlog.processor.impl.DefaultFilterTableProcessor;
import com.reyco.core.binlog.processor.impl.DefaultLogPostProcessor;
import com.reyco.core.binlog.service.impl.DefaultOperationLogService;

/**
 * 
 * @author Reyco
 *
 */
public abstract class AbstractParseBinLog implements ParseBinLog, ApplicationContextAware {
	// 字段更新状态：成功
	protected final static Boolean UPDATE_SUCCESS = true;
	// 字段更新状态：失败
	protected final static Boolean UPDATE_FAIL = false;

	// 操作事件：更新
	protected final static String UPDATE_EVENT = "update";
	// 操作事件：新增
	protected final static String INSERT_EVENT = "insert";
	// 操作事件：删除
	protected final static String DELETE_EVENT = "delete";

	public ApplicationContext applicationContext;
	// 过滤库表处理器
	protected final List<FilterTablePostProcessor> filterTablePostProcessors = new ArrayList<FilterTablePostProcessor>();
	// 处理数据
	protected final List<LogPostProcessor> logPostProcessors = new ArrayList<LogPostProcessor>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 解析parseEntry
	 * 
	 * @param entrys
	 */
	abstract void parseEntry(CanalEntry.Entry entry) throws Exception;

	/**
	 * 解析RowData
	 * 
	 * @param entry
	 */
	abstract void parseRowData(List<RowData> rowDatas, String database, String tableName, EventType eventType)
			throws Exception;

	/**
	 * 处理delete的事件结果集
	 * 
	 * @param rowData
	 * @param database
	 * @param tableName
	 * @return
	 */
	protected List<LogDefinition> getLogDefinitionByDelete(RowData rowData, String database, String tableName,
			String tablePrimaryKey) {
		// 返回的结果
		List<LogDefinition> definitions = new ArrayList<>();
		// 删除前的数据
		List<Column> beforeColumnsList = rowData.getBeforeColumnsList();
		//
		LogDefinition logDefinition = null;

		Long recordId = null;
		for (CanalEntry.Column column : beforeColumnsList) {
			if (recordId != null) {
				break;
			}
			String name = column.getName();
			if (name.equalsIgnoreCase(tablePrimaryKey)) {
				recordId = Long.valueOf(column.getValue());
			}
		}
		// 遍历所有的字段
		for (CanalEntry.Column column : beforeColumnsList) {
			if (column.getUpdated()) { // 是否更新过
				logDefinition = new LogDefinition();
				String columnName = column.getName();
				String columnValue = column.getValue();
				logDefinition.setUpdated(UPDATE_SUCCESS);
				logDefinition.setDatabaseName(database);
				logDefinition.setTableName(tableName);
				logDefinition.setRecordId(recordId);
				logDefinition.setEventType(DELETE_EVENT);
				logDefinition.setColumn(columnName);
				logDefinition.setBeforeValue(columnValue);
				definitions.add(logDefinition);
			}
		}
		return definitions;
	}

	/**
	 * 处理insert的事件结果集
	 * 
	 * @param rowData
	 * @param database
	 * @param tableName
	 * @return
	 */
	protected List<LogDefinition> getLogDefinitionByInsert(RowData rowData, String database, String tableName,
			String tablePrimaryKey, String updateByColumn, String updateTmColumn) {
		// 返回的结果
		List<LogDefinition> definitions = new ArrayList<>();
		// 新增后的数据
		List<Column> afterColumnsList = rowData.getAfterColumnsList();
		Long recordId = null;
		// 操作人
		String operationName = null;
		// 操作事件
		Long operationTime = null;
		for (CanalEntry.Column column : afterColumnsList) {
			if (recordId != null && operationName != null && operationTime != null) {
				break;
			}
			String name = column.getName();
			if (name.equalsIgnoreCase(updateByColumn)) {
				operationName = column.getValue();
			} else if (name.equalsIgnoreCase(updateTmColumn)) {
				operationTime = Long.valueOf(column.getValue());
			} else if (name.equalsIgnoreCase(tablePrimaryKey)) {
				recordId = Long.valueOf(column.getValue());
			}
		}
		//
		LogDefinition logDefinition = null;
		// 遍历所有的字段
		for (CanalEntry.Column column : afterColumnsList) {
			if (column.getUpdated()) {
				logDefinition = new LogDefinition();
				String columnName = column.getName();
				String columnValue = column.getValue();
				logDefinition.setUpdated(UPDATE_SUCCESS);
				logDefinition.setDatabaseName(database);
				logDefinition.setTableName(tableName);
				logDefinition.setRecordId(recordId);
				logDefinition.setEventType(INSERT_EVENT);
				logDefinition.setColumn(columnName);
				logDefinition.setAftreValue(columnValue);
				logDefinition.setOperationName(operationName);
				logDefinition.setOperationTime(operationTime);
				definitions.add(logDefinition);
			}
		}
		return definitions;
	}

	/**
	 * 处理update的事件结果集
	 * 
	 * @param rowData
	 * @param database
	 * @param tableName
	 * @return
	 */
	protected List<LogDefinition> getLogDefinitionByUpdate(RowData rowData, String database, String tableName,
			String tablePrimaryKey, String updateByColumn, String updateTmColumn) {
		// 返回的结果
		List<LogDefinition> definitions = new ArrayList<>();
		//
		LogDefinition logDefinition = null;
		// 更新前后
		List<Column> beforeColumnsList = rowData.getBeforeColumnsList();
		List<Column> afterColumnsList = rowData.getAfterColumnsList();

		Long recordId = null;
		// 操作人
		String operationName = null;
		// 操作事件
		Long operationTime = null;
		for (CanalEntry.Column column : afterColumnsList) {
			if (recordId != null && operationName != null && operationTime != null) {
				break;
			}
			String name = column.getName();
			if (name.equalsIgnoreCase(updateByColumn)) {
				operationName = column.getValue();
			} else if (name.equalsIgnoreCase(updateTmColumn)) {
				operationTime = Long.valueOf(column.getValue());
			} else if (name.equalsIgnoreCase(tablePrimaryKey)) {
				recordId = Long.valueOf(column.getValue());
			}
		}
		// 遍历所有的字段
		for (CanalEntry.Column column : afterColumnsList) {
			if (column.getUpdated()) {
				logDefinition = new LogDefinition();
				String columnName = column.getName();
				String columnValue = column.getValue();
				logDefinition.setUpdated(UPDATE_SUCCESS);
				logDefinition.setDatabaseName(database);
				logDefinition.setTableName(tableName);
				logDefinition.setRecordId(recordId);
				logDefinition.setEventType(UPDATE_EVENT);
				logDefinition.setColumn(columnName);
				// 跟新前的字段value
				for (CanalEntry.Column beforeColumn : beforeColumnsList) {
					if (beforeColumn.getName().equals(columnName)) {
						Object beforeValue = beforeColumn.getValue();
						logDefinition.setBeforeValue(beforeValue);
						break;
					}
				}
				logDefinition.setAftreValue(columnValue);
				logDefinition.setOperationName(operationName);
				logDefinition.setOperationTime(operationTime);
				definitions.add(logDefinition);
			}
		}
		return definitions;
	}
	/**
	 * 注册FilterTablePostProcessors
	 */
	public void registrFilterTablePostProcessors() {
		// Data数据拿到之后执行相应的FilterTablePostProcessor
		Map<String, FilterTablePostProcessor> filterTablePostProcessorMap = applicationContext
				.getBeansOfType(FilterTablePostProcessor.class);
		if (filterTablePostProcessorMap != null && !filterTablePostProcessorMap.isEmpty()) {
			// 转成list
			List<FilterTablePostProcessor> filterTablePostProcessorList = new ArrayList<FilterTablePostProcessor>(
					filterTablePostProcessorMap.values());
			// 排序
			// First, register the FilterTablePostProcessors that implement PriorityOrdered.
			List<FilterTablePostProcessor> priorityOrderedPostProcessors = new ArrayList<FilterTablePostProcessor>();
			Iterator<FilterTablePostProcessor> iteratorPriorityOrdered = filterTablePostProcessorList.iterator();
			while (iteratorPriorityOrdered.hasNext()) {
				FilterTablePostProcessor ftpp = iteratorPriorityOrdered.next();
				if (ftpp instanceof PriorityOrdered) {
					priorityOrderedPostProcessors.add(ftpp);
					iteratorPriorityOrdered.remove();
				}
			}
			sortPostProcessors(priorityOrderedPostProcessors);
			filterTablePostProcessors.addAll(priorityOrderedPostProcessors);

			// Next, register the FilterTablePostProcessors that implement Ordered.
			List<FilterTablePostProcessor> orderedPostProcessors = new ArrayList<FilterTablePostProcessor>(3);
			Iterator<FilterTablePostProcessor> iteratorOrdered = filterTablePostProcessorList.iterator();
			while (iteratorOrdered.hasNext()) {
				FilterTablePostProcessor ftpp = iteratorOrdered.next();
				Order order = ftpp.getClass().getAnnotation(Order.class);
				if (ftpp instanceof Ordered || order != null) {
					orderedPostProcessors.add(ftpp);
					iteratorOrdered.remove();
				}
			}
			sortPostProcessors(orderedPostProcessors);
			filterTablePostProcessors.addAll(orderedPostProcessors);

			// Now, register all regular FilterTablePostProcessors.
			List<FilterTablePostProcessor> nonOrderPostProcessors = new ArrayList<FilterTablePostProcessor>();
			// First, register the FilterTablePostProcessors that implement PriorityOrdered.
			Iterator<FilterTablePostProcessor> iteratorNonOrdered = filterTablePostProcessorList.iterator();
			while (iteratorNonOrdered.hasNext()) {
				FilterTablePostProcessor ftpp = iteratorNonOrdered.next();
				nonOrderPostProcessors.add(ftpp);
				iteratorNonOrdered.remove();
			}
			filterTablePostProcessors.addAll(nonOrderPostProcessors);
		}
	}

	/**
	 * 注册LogPostProcessors
	 */
	public void registerLogPostProcessors() {
		// logDefinition初始化之后执行相应的logPostProcessor
		Map<String, LogPostProcessor> logPostProcessorMap = applicationContext.getBeansOfType(LogPostProcessor.class);
		if (logPostProcessorMap != null && !logPostProcessorMap.isEmpty()) {
			// 转成list
			List<LogPostProcessor> logPostProcessorList = new ArrayList<LogPostProcessor>(logPostProcessorMap.values());
			// 排序
			// First, register the LogPostProcessors that implement PriorityOrdered.
			List<LogPostProcessor> priorityOrderedPostProcessors = new ArrayList<LogPostProcessor>(3);
			Iterator<LogPostProcessor> iteratorPriorityOrdered = logPostProcessorList.iterator();
			while (iteratorPriorityOrdered.hasNext()) {
				LogPostProcessor lpp = iteratorPriorityOrdered.next();
				if (lpp instanceof PriorityOrdered) {
					priorityOrderedPostProcessors.add(lpp);
					iteratorPriorityOrdered.remove();
				}
			}
			sortPostProcessors(priorityOrderedPostProcessors);
			logPostProcessors.addAll(priorityOrderedPostProcessors);

			// Next, register the LogPostProcessors that implement Ordered.
			List<LogPostProcessor> orderedPostProcessors = new ArrayList<LogPostProcessor>(3);
			Iterator<LogPostProcessor> iteratorOrdered = logPostProcessorList.iterator();
			while (iteratorOrdered.hasNext()) {
				LogPostProcessor lpp = iteratorOrdered.next();
				Order order = lpp.getClass().getAnnotation(Order.class);
				if (lpp instanceof Ordered || order != null) {
					orderedPostProcessors.add(lpp);
					iteratorOrdered.remove();
				}
			}
			sortPostProcessors(orderedPostProcessors);
			logPostProcessors.addAll(orderedPostProcessors);

			// Now, register all regular LogPostProcessors.
			List<LogPostProcessor> nonOrderPostProcessors = new ArrayList<LogPostProcessor>(3);
			// First, register the LogPostProcessors that implement PriorityOrdered.
			Iterator<LogPostProcessor> iteratorNonOrdered = logPostProcessorList.iterator();
			while (iteratorNonOrdered.hasNext()) {
				LogPostProcessor lpp = iteratorNonOrdered.next();
				nonOrderPostProcessors.add(lpp);
				iteratorNonOrdered.remove();
			}
			logPostProcessors.addAll(nonOrderPostProcessors);
		}
	}

	/**
	 *
	 * Invoke FilterTable processors registered as logs in the context.
	 */
	protected Boolean invokeFilterTablePostProcessors(String database, String tableName) {
		// Finally, invoke all internal BeanPostProcessors.
		if (!filterTablePostProcessors.isEmpty()) {
			for (FilterTablePostProcessor filterTablePostProcessor : filterTablePostProcessors) {
				if (!filterTablePostProcessor.fiterTable(database, tableName)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 *
	 * Invoke log processors registered as logs in the context.
	 */
	protected void invokeLogPostProcessors(List<LogDefinition> logDefinitions, String tableName) {
		// Finally, invoke all internal BeanPostProcessors.
		if (!logPostProcessors.isEmpty()) {
			for (LogPostProcessor logPostProcessor : logPostProcessors) {
				logPostProcessor.postProcessAftereInitialization(logDefinitions, tableName);
			}
		}
	}

	/**
	 * 排序
	 * 
	 * @param postProcessors
	 */
	private static void sortPostProcessors(List<?> postProcessors) {
		Comparator<Object> comparatorToUse = new AnnotationAwareOrderComparator();
		postProcessors.sort(comparatorToUse);
	}
	@Bean
	public DefaultFilterTableProcessor defaultFilterTableProcessor() {
		DefaultFilterTableProcessor defaultFilterTableProcessor = new DefaultFilterTableProcessor();
		return defaultFilterTableProcessor;
	}
	@Bean
	public DefaultLogPostProcessor defaultLogPostProcessor() {
		DefaultLogPostProcessor defaultLogPostProcessor = new DefaultLogPostProcessor();
		return defaultLogPostProcessor;
	}
	
	@Bean
	public DefaultOperationLogService defaultOperationLogService() {
		DefaultOperationLogService defaultOperationLogService = new DefaultOperationLogService();
		return defaultOperationLogService;
	}
	@Bean
	public IndexController indexController() {
		IndexController indexController = new IndexController();
		return indexController;
	}
}