package com.batis.bean;

import java.util.Map;

/**
 * mybatis配置上下文对象
 * @author 紫马
 *
 */
public class MybatisCfg {
	
	private String properties;
	
	private String defaultEnv;
	
	private Map<String,Environment> environments;
	
	private Map<String,Mapper> mappers;

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getDefaultEnv() {
		return defaultEnv;
	}

	public void setDefaultEnv(String defaultEnv) {
		this.defaultEnv = defaultEnv;
	}

	public Map<String, Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(Map<String, Environment> environments) {
		this.environments = environments;
	}

	public Map<String, Mapper> getMappers() {
		return mappers;
	}

	public void setMappers(Map<String, Mapper> mappers) {
		this.mappers = mappers;
	}
	
}
