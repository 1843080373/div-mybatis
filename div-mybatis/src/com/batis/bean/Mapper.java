package com.batis.bean;

import java.util.Map;

public class Mapper {

	private  String namespace;

	private Map<String,ResultMap> resultMaps;
	
	private Map<String,OpeateTag> opeateTagMaps;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Map<String, ResultMap> getResultMaps() {
		return resultMaps;
	}

	public void setResultMaps(Map<String, ResultMap> resultMaps) {
		this.resultMaps = resultMaps;
	}

	public Map<String, OpeateTag> getOpeateTagMaps() {
		return opeateTagMaps;
	}

	public void setOpeateTagMaps(Map<String, OpeateTag> opeateTagMaps) {
		this.opeateTagMaps = opeateTagMaps;
	}

}
