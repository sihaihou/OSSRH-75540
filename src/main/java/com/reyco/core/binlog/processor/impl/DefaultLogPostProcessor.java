package com.reyco.core.binlog.processor.impl;

import java.util.List;

import org.springframework.core.PriorityOrdered;

import com.reyco.core.binlog.model.LogDefinition;
import com.reyco.core.binlog.processor.AbstractLogPostProcessor;

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
