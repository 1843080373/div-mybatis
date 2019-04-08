package com.batis.bean.tag;

import java.util.LinkedList;

public class Choose {

	private LinkedList<When> whenLists;
	private String otherwise;
	public LinkedList<When> getWhenLists() {
		return whenLists;
	}
	public void setWhenLists(LinkedList<When> whenLists) {
		this.whenLists = whenLists;
	}
	public String getOtherwise() {
		return otherwise;
	}
	public void setOtherwise(String otherwise) {
		this.otherwise = otherwise;
	}
	
}
