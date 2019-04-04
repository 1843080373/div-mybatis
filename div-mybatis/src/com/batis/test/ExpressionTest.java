package com.batis.test;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;

public class ExpressionTest {
	public static void main(String[] args) {
		Map<String, Object> env = new HashMap<String, Object>();
		Object foo = new HashMap<String, Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("userName", "����");
				put("phone", "1807657569");
				put("weigth", 180);
				put("sex", "M");
			}
		};
		String test = "foo.userName == '����' && foo.weigth >= 180 && foo.sex=='M'";
		env.put("foo", foo);
		System.out.println(AviatorEvaluator.execute(test, env));
	}
}
