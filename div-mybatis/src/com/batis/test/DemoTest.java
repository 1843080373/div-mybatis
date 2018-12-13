package com.batis.test;

import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.batis.core.SqlSession;
import com.batis.core.SqlSessionFactory;
import com.batis.core.SqlSessionFactoryBuilder;
import com.batis.mapper.UserMapper;
import com.batis.po.User;

public class DemoTest {

	public static void main(String[] args) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("UserMapper.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User();
		user.setUserId(1);
		user.setUserName("张三1");
		user.setPassword("123456111");
		user.setPhone("18076575691111");
		//userMapper.save(user);
		//userMapper.update(user);
		//int rows = userMapper.delete(1);
		//System.out.println(rows);
		/*List<User> list=userMapper.list();
		System.out.println(JSONObject.toJSON(list));*/
		List<User> list=userMapper.listCondition("张三");
		System.out.println(JSONObject.toJSON(list));
	}
}
