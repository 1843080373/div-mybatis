package com.batis.bean;

public class Result {

	private String column;
	private String property;
	private String jdbcType;
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public Result(String column, String property, String jdbcType) {
		super();
		this.column = column;
		this.property = property;
		this.jdbcType = jdbcType;
	}
	public Result() {
		super();
	}
	
}
