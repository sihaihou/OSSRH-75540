package com.reyco.core.binlog.processor;

/**
 * 
 * @author reyco
 *
 */
public abstract class AbstractFilterPostProcessor implements FilterTablePostProcessor{

	@Override
	public Boolean fiterTable(String database, String tableName) {
		/*
		 * 过滤掉的DB
		 */
		Boolean postProcessorDatabase = postProcessorDatabase(database);
		if(!postProcessorDatabase) {
			return false;
		}
		/*
		 * 过滤的库对应的表
		 */
		return postProcessorTable(database,tableName);
	}
	/**
	 * 过滤库
	 * @param databaseName	binlog的库
	 * @return
	 */
	protected abstract Boolean postProcessorDatabase(String databaseName);
	/**
	 * 
	 * @param databaseName	binlog的库
	 * @param tableName		binlog的表
	 * @return
	 */
	protected abstract Boolean postProcessorTable(String databaseName,String tableName);
}
