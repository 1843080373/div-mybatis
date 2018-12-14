package com.batis.bean;

/**
 * 数据源列表
 * @author 紫马
 *
 */
public class Environment {

	private String id;
	
	private String transactionManagerType;
	
	private MybatisDataSource mybatisDataSource;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransactionManagerType() {
		return transactionManagerType;
	}

	public void setTransactionManagerType(String transactionManagerType) {
		this.transactionManagerType = transactionManagerType;
	}

	public MybatisDataSource getMybatisDataSource() {
		return mybatisDataSource;
	}

	public void setMybatisDataSource(MybatisDataSource mybatisDataSource) {
		this.mybatisDataSource = mybatisDataSource;
	}
	
}
