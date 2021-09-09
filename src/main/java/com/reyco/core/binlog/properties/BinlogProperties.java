package com.reyco.core.binlog.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binlog
 * @author housihai
 *
 */
@ConfigurationProperties(prefix=BinlogProperties.BINLOG_PREFIX)
public class BinlogProperties {
	
	public final static String BINLOG_PREFIX = "binlog";
	/**
	 * 	忽略接受日志的实现对象
	 */
	private String excludeOperationLogServiceNames = "com.fdgj.binlog.service.impl.DefaultOperationLogService";
	/**
	 * service信息
	 */
	private ServerConfig serverConfig;
	/**
	 * 需要监听的库：Packages to search type aliases. (Package delimiters are ",; \t\n")
	 */
	private String listenDatabases;
	/**
	 * 过滤掉的库：Packages to search type aliases. (Package delimiters are ",; \t\n")
	 */
	private String excludeDatabases;
	/**
	 * <pre>
	 * 需要监听的表：(value: Packages to search type aliases. (Package delimiters are ",; \t\n"))
	 * 
	 * 监听库对应的表:value(abc*:  支持abc开头的通配符*)
	 * </pre>
	 */
	private Map<String,String> listenTable;
	/**
	 * <pre>
	 * 过滤掉对应库的表:
	 * 			key:库名
	 * 			value:表名     Packages to search type aliases. (Package delimiters are ",; \t\n")
	 * 
	 * 过滤掉对应库的表:value(abc*:  支持abc开头的通配符*)
	 * </pre>
	 */
	private Map<String,String> excludeTable;
	
	//主键id：
	private String primaryKey = "id";
	//更新人：字段
	private String updateByColumn = "upd_by";
	//更新时间：字段
	private String updateTmColumn = "upd_tm";
	public ServerConfig getServerConfig() {
		return serverConfig;
	}
	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}
	public String getListenDatabases() {
		return listenDatabases;
	}
	public void setListenDatabases(String listenDatabases) {
		this.listenDatabases = listenDatabases;
	}
	public String getExcludeDatabases() {
		return excludeDatabases;
	}
	public void setExcludeDatabases(String excludeDatabases) {
		this.excludeDatabases = excludeDatabases;
	}
	public Map<String, String> getListenTable() {
		return listenTable;
	}
	public void setListenTable(Map<String, String> listenTable) {
		this.listenTable = listenTable;
	}
	public Map<String, String> getExcludeTable() {
		return excludeTable;
	}
	public void setExcludeTable(Map<String, String> excludeTable) {
		this.excludeTable = excludeTable;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getUpdateByColumn() {
		return updateByColumn;
	}
	public void setUpdateByColumn(String updateByColumn) {
		this.updateByColumn = updateByColumn;
	}
	public String getUpdateTmColumn() {
		return updateTmColumn;
	}
	public void setUpdateTmColumn(String updateTmColumn) {
		this.updateTmColumn = updateTmColumn;
	}
	public static String getBinlogPrefix() {
		return BINLOG_PREFIX;
	}
	public String getExcludeOperationLogServiceNames() {
		return excludeOperationLogServiceNames;
	}
	public void setExcludeOperationLogServiceNames(String excludeOperationLogServiceNames) {
		this.excludeOperationLogServiceNames = excludeOperationLogServiceNames;
	}
}
