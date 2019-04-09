package com.jwt.demo;

import java.io.Serializable;

public class Hobby implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Hobby(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
	public Hobby() {
		super();
	}
	@Override
	public String toString() {
		return "Hobby [name=" + name + ", age=" + age + "]";
	}
	
}
