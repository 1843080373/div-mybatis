package com.batis.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern {
	public static void main(String[] args) {
		String contents = " update user" + 
				"    set user_name = #{userName,jdbcType=VARCHAR}," + 
				"      password = #{password,jdbcType=VARCHAR}," + 
				"      phone = #{phone,jdbcType=VARCHAR}" + 
				"    where user_id = #{userId,jdbcType=INTEGER}";// 以java开头,以>结束的字符串
		Pattern p = Pattern.compile("\\{[^\\}]*\\}");
		Matcher m = p.matcher(contents);
		while (m.find()) {
			String item=contents.substring(m.start(), m.end());
			System.out.println(item);
			String newItem=item.substring(1, item.length()-1);
			String[] array=newItem.split(",");
			String fieldNamee=array[0];
			String jdbcType=array[1].substring("jdbcType=".length());
			System.out.println(fieldNamee+","+jdbcType);
		}
		System.out.println(contents.replaceAll("\\{[^\\}]*\\}", "?").replaceAll("#", "").replaceAll("   ", ""));
	}
}