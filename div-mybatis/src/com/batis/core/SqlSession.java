package com.batis.core;

import java.sql.Connection;
import java.util.Map;

import com.batis.bean.Mapper;

public class SqlSession {

	private Connection connection;
	private Map<String,Mapper> mapperMaps;
	
	public Map<String, Mapper> getMapperMaps() {
		return mapperMaps;
	}
	public Connection getConnection() {
		return connection;
	}
	public SqlSession(Connection connection, Map<String, Mapper> mapperMaps) {
		super();
		this.connection = connection;
		this.mapperMaps = mapperMaps;
	}


	public <T> T getMapper(Class<T> c) {
		MapperProxy<T> mapperProxy = new MapperProxy<T>();
		mapperProxy.setSqlSession(this);
		return (T) mapperProxy.getInstance(c);
	}
}
