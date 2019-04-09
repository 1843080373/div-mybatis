package com.batis.test;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.batis.core.SqlSession;
import com.batis.core.SqlSessionFactory;
import com.batis.core.SqlSessionFactoryBuilder;
import com.batis.mapper.AnswerMapper;
import com.batis.mapper.UserMapper;
import com.batis.po.Answer;
import com.batis.po.User;

public class DemoTest {

	public static void main(String[] args) {
		testUser();
	}

	@SuppressWarnings("unused")
	private static void testAnswer() {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mybatis.cfg.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		AnswerMapper answerMapper = sqlSession.getMapper(AnswerMapper.class);
		Answer record = new Answer();
		record.setId(98717);
		record.setChoiceId(11);
		record.setEnabled(true);
		record.setQuestionnaireId(22);
		record.setUserId(11);
		record.setVersion(0);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		// int rows=answerMapper.insert(record);
		// int rows=answerMapper.deleteByPrimaryKey(98715);
		// int rows=answerMapper.updateByPrimaryKey(record);
		// System.out.println(rows);
		// List<Answer> list=answerMapper.selectAll();
		List<Answer> list = answerMapper.selectByPrimaryKey(2);
		System.out.println(JSONObject.toJSON(list));
	}

	private static void testUser() {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mybatis.cfg.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = new User();
		user.setUserId(1);
		user.setUserName("张三");
		user.setPassword("123456");
		user.setPhone("1807657569");
		// int rows = userMapper.save(user);
		// int rows = userMapper.update(user);
		// int rows = userMapper.delete(2);
		// System.out.println(rows);
		/*
		 * List<User> list=userMapper.list();
		 * System.out.println(JSONObject.toJSON(list));
		 */
		// List<User> list=userMapper.listCondition("22");
		// System.out.println(JSONObject.toJSON(list));
		/*
		 * List<User> list=userMapper.queryCondition3(new HashMap<String, Object>(){
		 *//**
			* 
			*//*
				 * private static final long serialVersionUID = 1L;
				 * 
				 * { put("userName", "张三"); put("phone", "1807657569"); } });
				 */
		/*
		 * List<User> list = userMapper.listCondition2(new User(null, null, null,
		 * "张三"));
		 */
		// List<User> list = userMapper.listCondition1("张三");
		
		/*
		 * List<User> list = userMapper.queryCondition6(new HashMap<String, Object>() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * { put("ids",new Object[] { 1, 1139, 1141}); } });
		 */
		
		/*
		 * List<User> list = userMapper.queryCondition5(new HashMap<String, Object>() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * { put("userName","张"); put("phone", "1807657569"); } });
		 */
		//System.out.println(JSONObject.toJSON(list));
		 
		userMapper.updateCondition(user);
	}
}
