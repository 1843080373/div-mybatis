package com.batis.mapper;

import java.util.List;

import com.batis.po.User;

public interface UserMapper {

	public int save(User user);
	
	public int update(User user);
	
	public int delete(Integer id);
	
	public List<User> list();
	
	public List<User> listCondition(String name);

}
