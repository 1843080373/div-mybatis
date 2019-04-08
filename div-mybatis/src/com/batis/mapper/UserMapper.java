package com.batis.mapper;

import java.util.List;
import java.util.Map;

import com.batis.po.User;

public interface UserMapper {

	public int save(User user);
	
	public int update(User user);
	
	public int delete(Integer id);
	
	public List<User> list();
	
	public List<User> listCondition1(String name);
	
	public List<User> listCondition2(User user);
	
	public List<User> queryCondition3(Map<String,Object> param);
	
	public List<User> queryCondition4(Map<String,Object> param);
	
	public int updateCondition(User user);
	
	public List<User> queryCondition5(Map<String,Object> param);
	
	public List<User> queryCondition6(Map<String,Object> param);

}
