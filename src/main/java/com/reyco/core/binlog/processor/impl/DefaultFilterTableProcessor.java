package com.reyco.core.binlog.processor.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import com.reyco.core.binlog.autoConfigure.ParseLogAutoConfigure;
import com.reyco.core.binlog.commons.StringUtil;
import com.reyco.core.binlog.processor.AbstractFilterPostProcessor;
import com.reyco.core.binlog.properties.BinlogProperties;

public class DefaultFilterTableProcessor extends AbstractFilterPostProcessor implements PriorityOrdered{
	
	private static Logger logger = LoggerFactory.getLogger(ParseLogAutoConfigure.class);
	
	@Autowired
	private BinlogProperties binlogProperties;
	
	@Override
	public Boolean postProcessorDatabase(String databaseName) {
		//过滤需要监听的库：true不过滤   false过滤 
		Boolean postProcessorListenDatabase = postProcessorListenDatabase(databaseName);
		if(!postProcessorListenDatabase) {
			return false;
		}
		//过滤库： true不过滤   false过滤 
		Boolean postProcessorExcludeDatabase = postProcessorExcludeDatabase(databaseName);
		return postProcessorExcludeDatabase;
	}

	@Override
	public Boolean postProcessorTable(String databaseName, String tableName) {
		Boolean postProcessorlistenTable = postProcessorlistenTable(databaseName, tableName);
		if(!postProcessorlistenTable) {
			return false;
		}
		return postProcessorExcludeTable(databaseName, tableName);
	}
	
	/**
	 * 过滤库： true不过滤   false过滤  
	 * @param database
	 * @return
	 * @throws FilterException
	 */
	private Boolean postProcessorListenDatabase(String database){
		String listenDatabase = binlogProperties.getListenDatabases();
		if(StringUtils.isBlank(listenDatabase)) {
			return true;
		}
		String[] listenDatabases = StringUtil.strToArray(listenDatabase);
		if(listenDatabases.length<1) {
			logger.debug("没有指定需要监听的database,默认监听所有的库");
			return true;
		}else {
			for (String listenDatabaseTemp : listenDatabases) {
				if(listenDatabaseTemp.equalsIgnoreCase(database)) {
					logger.debug("在需要监听的database:"+database);
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 过滤库： true不过滤   false过滤  
	 * @param database
	 * @return
	 * @throws FilterException
	 */
	private Boolean postProcessorExcludeDatabase(String database){
		String excludeDatabase = binlogProperties.getExcludeDatabases();
		if(StringUtils.isBlank(excludeDatabase)) {
			return true;
		}
		String[] excludeDatabases = StringUtil.strToArray(excludeDatabase);
		if(excludeDatabases.length<1) {
			logger.debug("没有过滤掉的database");
			return true;
		}
		for (String temp : excludeDatabases) {
			if(temp.equalsIgnoreCase(database)) {
				logger.debug("过滤掉的database:"+database);
				return false;
			}
		}
		return true;
	}
	/**
	 * 过滤表
	 * @param database
	 * @param tableName
	 * @return
	 * @throws FilterException
	 */
	private Boolean postProcessorlistenTable(String database,String tableName){
		Map<String, String> listenTableMap = binlogProperties.getListenTable();
		if(listenTableMap==null || listenTableMap.isEmpty()) {
			return true;
		}
		for (String listenDatabase : listenTableMap.keySet()) {
			String listenDatabaseStr = listenTableMap.get(listenDatabase);
			if(StringUtils.isBlank(listenDatabaseStr)) {
				return true;
			}
			String[] listenTableArray = StringUtil.strToArray(listenDatabaseStr);
			if(listenTableArray == null || listenTableArray.length<1) {
				return false;
			}
			for (String listenTable : listenTableArray) {
				if(listenTable.contains("*")) {
					String begin = listenTable.split("\\*")[0];
					if(tableName.length()>begin.length()) {
						String dest = tableName.substring(0, begin.length());
						if(begin.equalsIgnoreCase(dest)) {
							logger.debug("监听的table:"+tableName);
							return true;
						}
					}
				}else {
					if(listenTable.equalsIgnoreCase(tableName)) {
						logger.debug("监听的table:"+tableName);
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 过滤表
	 * @param database
	 * @param tableName
	 * @return
	 * @throws FilterException
	 */
	private Boolean postProcessorExcludeTable(String database,String tableName){
		Map<String, String> excludeTableMap = binlogProperties.getExcludeTable();
		if(excludeTableMap==null || excludeTableMap.isEmpty()) {
			return true;
		}
		for (String excludeDatabase : excludeTableMap.keySet()) {
			String excludeTableStr = excludeTableMap.get(excludeDatabase);
			String[] excludeTableArray = StringUtil.strToArray(excludeTableStr);
			if(excludeTableArray == null || excludeTableArray.length<1) {
				return true;
			}
			for (String excludeTable : excludeTableArray) {
				if(excludeTable.contains("*")) {
					String begin = excludeTable.split("\\*")[0];
					if(tableName.length()>begin.length()) {
						String dest = tableName.substring(0, begin.length());
						if(begin.equalsIgnoreCase(dest)) {
							logger.debug("过滤掉的table:"+tableName);
							return false;
						}
					}
				}else {
					if(excludeTable.equalsIgnoreCase(tableName)) {
						logger.debug("过滤掉的table:"+tableName);
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}	
