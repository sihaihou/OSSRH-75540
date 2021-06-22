package com.fdgj.binlog.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.fdgj.binlog.autoConfigure.ParseLogAutoConfigure;
import com.fdgj.binlog.controller.IndexController;
import com.fdgj.binlog.processor.impl.DefaultFilterTableProcessor;
import com.fdgj.binlog.processor.impl.DefaultLogPostProcessor;
import com.fdgj.binlog.service.impl.DefaultOperationLogService;

public class CustomizedImportSelector implements ImportSelector{
	
	private static final String[] IMPORTS = {
			ParseLogAutoConfigure.class.getName(),			//日志解析对象
			DefaultFilterTableProcessor.class.getName(),    //日志库表过滤器
			DefaultLogPostProcessor.class.getName(),		//logDefinition的处理器
			DefaultOperationLogService.class.getName(),		//操作日志默认处理器
			IndexController.class.getName()					//对外接口
			};
	
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return IMPORTS;
	}
	
	
}