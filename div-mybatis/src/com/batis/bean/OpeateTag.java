package com.batis.bean;

import java.util.List;

public class OpeateTag {
	private String id;
	private String parameterType;
	private String resultMap;
	private String opeate;
	private Sql sql;
	private List<If> ifList;
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
	public List<If> getIfList() {
		return ifList;
	}
	public void setIfList(List<If> ifList) {
		this.ifList = ifList;
	}
	
}
