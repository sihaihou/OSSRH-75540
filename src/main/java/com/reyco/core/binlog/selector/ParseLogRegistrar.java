package com.reyco.core.binlog.selector;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.reyco.core.binlog.autoConfigure.ParseLogAutoConfigure;

/** 
 * @author  reyco
 * @date    2021.09.09
 * @version v1.0.1 
 */
public class ParseLogRegistrar implements ImportBeanDefinitionRegistrar{
	
	public final static String AUTO_PARSELOG_BEAN_NAME = "auto_ParseLog_bean_name";
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition(ParseLogAutoConfigure.class);
		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(AUTO_PARSELOG_BEAN_NAME, beanDefinition);
	}
}
