package com.batis.utils;

public class StringUtils {

	public static String fistCharUpperCase(String src) {
		return src.substring(0, 1).toUpperCase() + src.substring(1);
	}
}
