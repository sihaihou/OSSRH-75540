package com.fdgj.binlog.commons;

/**
 * 
 * @author reyco
 *
 */
public class StringUtil {
	/**
	 * 字符串转String[]
	 * @param str
	 * @return
	 */
	public static String[] strToArray(String str) {
		String[] arr = null; 
		if(str == null || str.equals("")) {
			return new String[] {};
		}
		if(str.contains(",")) {
			arr = str.split(",");
		}else if(str.contains(";")) {
			arr = str.split(";");
		}else {
			arr = new String[] {str};
		}
		return arr;
	}
}
