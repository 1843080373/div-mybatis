package com.batis.bean;

/**
 * 数据库配置信息
 * 
 * @author 紫马
 *
 */
public class MybatisDataSource {
	/**
	 *数据源类型，分别是：POOLED,UNPOOLED,JNDI
	 */
	private String type;
	/**
	 * 驱动
	 */
	private String driver;
	/**
	 * 数据库服务器
	 */
	private String url;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public MybatisDataSource(String driver, String url, String username, String password) {
		super();
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public MybatisDataSource() {
		super();
	}

}
