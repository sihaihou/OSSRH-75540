package com.fdgj.binlog.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author reyco
 */
public class ToString implements Serializable {
	/**
	 * 过滤字段
	 */
	private static final Collection<String> fieldNames = new ArrayList<String>();

	/**
	 * 过滤字段输出
	 *
	 * @param obj	需要toString的对象
	 * @return toString后的字符串
	 */
	public static String toString(Object obj) {
		if (fieldNames.size() == 0) {
			return ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
		} else {
			return new ReflectionToStringBuilder(obj, ToStringStyle.SHORT_PREFIX_STYLE)
					.setExcludeFieldNames(fieldNames.toArray(new String[fieldNames.size()])).toString();
		}
	}

	/**
	 * 添加过滤字段
	 *
	 * @param fieldName
	 *            字段名称
	 */
	public static void addFilterField(String fieldName) {
		fieldNames.add(fieldName);
	}

	@Override
	public String toString() {
		return toString(this);
	}
}