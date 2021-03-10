package com.fdgj.binlog.processor;

import java.util.List;

import com.fdgj.binlog.model.LogDefinition;
/**
 * binlog的LogDefinition初始化之后保存之前操作
 * @author reyco
 *
 */
public interface LogPostProcessor {
	
	/**
	 * LogDefinition初始化之后保存之前操作
	 * @param logDefinition
	 */
	void postProcessAftereInitialization(List<LogDefinition> logDefinitions,Object...ob);
	
}
