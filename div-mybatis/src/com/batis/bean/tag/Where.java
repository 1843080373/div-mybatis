package com.batis.bean.tag;

import java.util.LinkedList;
import java.util.List;

public class Where {
	private String preSql;
	private Choose choose;
	private List<If> ifList;
	private LinkedList<Foreach> foreachList;
	
	public String getPreSql() {
		return preSql;
	}
	public void setPreSql(String preSql) {
		this.preSql = preSql;
	}
	public Choose getChoose() {
		return choose;
	}
	public void setChoose(Choose choose) {
		this.choose = choose;
	}
	public List<If> getIfList() {
		return ifList;
	}
	public void setIfList(List<If> ifList) {
		this.ifList = ifList;
	}
	public LinkedList<Foreach> getForeachList() {
		return foreachList;
	}
	public void setForeachList(LinkedList<Foreach> foreachList) {
		this.foreachList = foreachList;
	}
	
}
