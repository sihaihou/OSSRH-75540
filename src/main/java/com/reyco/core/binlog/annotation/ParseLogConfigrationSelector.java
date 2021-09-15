package com.reyco.core.binlog.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/** 
 * @author  reyco
 * @date    2021.09.15
 * @version v1.0.1 
 */
public class ParseLogConfigrationSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[] {ParseLogRegistrar.class.getName(),
				ParseLogConfiguration.class.getName()};
	}

}
