package com.batis.bean;

public class OpeateTag {
	private String id;
	private String parameterType;
	private String resultMap;
	private String opeate;
	private Sql sql;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	public String getResultMap() {
		return resultMap;
	}
	public void setResultMap(String resultMap) {
		this.resultMap = resultMap;
	}
	public String getOpeate() {
		return opeate;
	}
	public void setOpeate(String opeate) {
		this.opeate = opeate;
	}
	public Sql getSql() {
		return sql;
	}
	public void setSql(Sql sql) {
		this.sql = sql;
	}
	
}
