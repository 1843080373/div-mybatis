package com.batis.po;

public class User {
	private String password;
	private Integer userId;
	private String phone;
	private String userName;

	public User() {
		super();
	}

	public User(String password, Integer userId, String phone, String userName) {
		super();
		this.password = password;
		this.userId = userId;
		this.phone = phone;
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getPhone() {
		return phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "User [password=" + password + ", userId=" + userId + ", phone=" + phone + ", userName=" + userName
				+ "]";
	}

}
