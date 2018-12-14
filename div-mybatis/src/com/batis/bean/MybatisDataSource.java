package com.batis.bean;

/**
 * ���ݿ�������Ϣ
 * 
 * @author ����
 *
 */
public class MybatisDataSource {
	/**
	 *����Դ���ͣ��ֱ��ǣ�POOLED,UNPOOLED,JNDI
	 */
	private String type;
	/**
	 * ����
	 */
	private String driver;
	/**
	 * ���ݿ������
	 */
	private String url;
	/**
	 * �û���
	 */
	private String username;
	/**
	 * ����
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
