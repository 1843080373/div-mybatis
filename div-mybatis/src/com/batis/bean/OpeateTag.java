package com.batis.bean;

import java.util.LinkedList;
import java.util.List;

import com.batis.bean.tag.Choose;
import com.batis.bean.tag.Foreach;
import com.batis.bean.tag.If;
import com.batis.bean.tag.Set;
import com.batis.bean.tag.Sql;
import com.batis.bean.tag.Where;

public class OpeateTag {
	private String id;
	private String parameterType;
	private String resultMap;
	private String opeate;
	private String alias;
	private Sql sql;
	
	
	private List<If> ifList;
	private Choose choose;
	private Where where;
	private LinkedList<Foreach> foreachList;
	private Set set;
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
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
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
	public Choose getChoose() {
		return choose;
	}
	public void setChoose(Choose choose) {
		this.choose = choose;
	}
	
	public Where getWhere() {
		return where;
	}
	public void setWhere(Where where) {
		this.where = where;
	}
	public LinkedList<Foreach> getForeachList() {
		return foreachList;
	}
	public void setForeachList(LinkedList<Foreach> foreachList) {
		this.foreachList = foreachList;
	}
	public Set getSet() {
		return set;
	}
	public void setSet(Set set) {
		this.set = set;
	}
}
