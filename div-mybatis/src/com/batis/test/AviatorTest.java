package com.batis.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class AviatorTest {
    public static void main(String[] args) {
    	SimpleVO foo = new SimpleVO(100, 3.14f, new Date());
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("foo", foo);
        System.out.println(AviatorEvaluator.execute("'foo.i = '+foo.i", env));   // foo.i = 100
        System.out.println(AviatorEvaluator.execute("'foo.f = '+foo.f", env));   // foo.f = 3.14
        System.out.println(AviatorEvaluator.execute("'foo.date.year = '+(foo.date.year+1990)", env));  // foo.date.year = 2106
	}

	private static void m5() {
		final List<String> list = new ArrayList<String>();
	    list.add("hello");
	    list.add(" world");
	    final int[] array = new int[3];
	    array[0] = 0;
	    array[1] = 1;
	    array[2] = 3;
	    final Map<String, Date> map = new HashMap<String, Date>();
	    map.put("date", new Date());
	    Map<String, Object> env = new HashMap<String, Object>();
	    env.put("list", list);
	    env.put("array", array);
	    env.put("mmap", map);
	    System.out.println(AviatorEvaluator.execute("list[0]+list[1]", env));   // hello world
	    System.out.println(AviatorEvaluator.execute("'array[0]+array[1]+array[2]=' + (array[0]+array[1]+array[2])", env));  // array[0]+array[1]+array[2]=4
	    System.out.println(AviatorEvaluator.execute("'today is ' + mmap.date ", env));
	}

	private static void m4() {
		String expression = "a-(b-c)>100";
		// 编译表达式
		Expression compiledExp = AviatorEvaluator.compile(expression);
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("a", 360.3);
		env.put("b", 45);
		env.put("c", -199.100);
		// 执行表达式
		Boolean result = (Boolean) compiledExp.execute(env);
		System.out.println(result); // false
	}

	private static void m3() {
		// 注册函数
		AviatorEvaluator.addFunction(new AviatorTest().new AddFunction());
		System.out.println(AviatorEvaluator.execute("add(1, 2)")); // 3.0
		System.out.println(AviatorEvaluator.execute("add(add(1, 2), 100)")); // 103.0
	}

	private static void m2() {
		String yourName = "Michael";
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("yourName", yourName);
		String result = (String) AviatorEvaluator.execute(" 'hello ' + yourName ", env);
		System.out.println(result); // hello Michael
	}

	private static void m1() {
		Long result = (Long) AviatorEvaluator.execute("1+2+3");
		System.out.println(result);
	}

	class AddFunction extends AbstractFunction {
		@Override
		public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
			Number left = FunctionUtils.getNumberValue(arg1, env);
			Number right = FunctionUtils.getNumberValue(arg2, env);
			return new AviatorDouble(left.doubleValue() + right.doubleValue());
		}

		public String getName() {
			return "add";
		}
	}
}
