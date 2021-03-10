package com.fdgj.binlog.processor;

/**
 * before parse binlog to logDefinition
 * @author reyco
 *
 */
public interface FilterTablePostProcessor {
	
	/**
	 * 过滤库/表
	 * @param database
	 * @param tableName
	 * @return
	 */
	 Boolean fiterTable(String database,String tableName);
}
