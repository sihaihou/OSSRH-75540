package com.fdgj.binlog.processor.impl;

import java.util.List;

import org.springframework.core.PriorityOrdered;

import com.fdgj.binlog.model.LogDefinition;
import com.fdgj.binlog.processor.AbstractLogPostProcessor;

public class DefaultLogPostProcessor extends AbstractLogPostProcessor implements PriorityOrdered{
	
	@Override
	public void postProcessAftereInitialization(List<LogDefinition> logDefinitions, Object... ob) {
		// For subclasses: do nothing by default.
	}

	@Override
	public int getOrder() {
		return PriorityOrdered.HIGHEST_PRECEDENCE;
	}
	
}
