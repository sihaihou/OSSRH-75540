package com.fdgj.binlog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fdgj.binlog.model.LogDefinition;
import com.fdgj.binlog.service.OperationLogService;

public class OperationLogServiceContent {
	
	@Autowired
	private Map<String,OperationLogService> operationLogServiceMap = new HashMap<>();
	
	public void save(List<LogDefinition> logDefinitions,String logDefinitionName) throws Exception {
		operationLogServiceMap.get(logDefinitionName).save(logDefinitions);
	}
	
}
