package com.reyco.core.binlog.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.core.binlog.controller.IndexController;
import com.reyco.core.binlog.processor.impl.DefaultFilterTableProcessor;
import com.reyco.core.binlog.processor.impl.DefaultLogPostProcessor;
import com.reyco.core.binlog.service.impl.DefaultOperationLogService;

/** 
 * @author  reyco
 * @date    2021.09.15
 * @version v1.0.1 
 */
@Configuration
public class ParseLogConfiguration {
	
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
