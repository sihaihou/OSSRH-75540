package com.reyco.core.binlog.commons;

/**
 * 
 * @author reyco
 *
 */
public class StringUtil {
	/**
	 * 字符串转String[]
	 * @param strArray
	 * @return
	 */
	public static String[] strToArray(String strArray) {
		String[] arr = null; 
		if(strArray == null || strArray.equals("")) {
			return new String[] {};
		}
		if(strArray.contains(",")) {
			arr = strArray.split(",");
		}else if(strArray.contains(";")) {
			arr = strArray.split(";");
		}else {
			arr = new String[] {strArray};
		}
		return arr;
	}
}
