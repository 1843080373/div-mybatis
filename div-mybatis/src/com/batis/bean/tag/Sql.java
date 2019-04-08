package com.batis.bean.tag;

import java.util.LinkedHashMap;

public class Sql {
	private String sqlContent;
	private String psSql;
	private LinkedHashMap<String,String> paramTypes;
	public String getSqlContent() {
		return sqlContent;
	}
	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
	}
	public String getPsSql() {
		return psSql;
	}
	public void setPsSql(String psSql) {
		this.psSql = psSql;
	}
	public LinkedHashMap<String, String> getParamTypes() {
		return paramTypes;
	}
	public void setParamTypes(LinkedHashMap<String, String> paramTypes) {
		this.paramTypes = paramTypes;
	}
	
}
