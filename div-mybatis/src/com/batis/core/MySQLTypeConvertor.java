package com.batis.core;

/**
 * mysql类型转换器
 * 
 * @author 紫马
 *
 */
public class MySQLTypeConvertor implements TypeConvertor {

	@Override
	public String dbTypeToJavaType(String dbType) {
		if ("VARCHAR".equalsIgnoreCase(dbType) || "char".equalsIgnoreCase(dbType)) {
			return "java.lang.String";
		} else if ("int".equalsIgnoreCase(dbType) || "tinyint".equalsIgnoreCase(dbType)
				|| "smallint".equalsIgnoreCase(dbType) || "integer".equalsIgnoreCase(dbType)) {
			return "java.lang.Integer";
		} else if ("bigint".equalsIgnoreCase(dbType)) {
			return "java.lang.Long";
		} else if ("double".equalsIgnoreCase(dbType) || "float".equalsIgnoreCase(dbType)) {
			return "java.lang.Double";
		} else if ("Clob".equalsIgnoreCase(dbType)) {
			return "java.lang.Clob";
		} else if ("Blob".equalsIgnoreCase(dbType)) {
			return "java.lang.Blob";
		} else if ("timestamp".equalsIgnoreCase(dbType)) {
			return "java.util.Date";
		}else if ("bit".equalsIgnoreCase(dbType)) {
			return "java.lang.Boolean";
		}
		return null;
	}
	
	public String dbType2JavaType(String dbType) {
		if ("VARCHAR".equalsIgnoreCase(dbType) || "char".equalsIgnoreCase(dbType)) {
			return "java.lang.String";
		} else if ("int".equalsIgnoreCase(dbType) || "tinyint".equalsIgnoreCase(dbType)
				|| "smallint".equalsIgnoreCase(dbType) || "integer".equalsIgnoreCase(dbType)) {
			return "java.lang.Integer";
		} else if ("bigint".equalsIgnoreCase(dbType)) {
			return "java.lang.Long";
		} else if ("double".equalsIgnoreCase(dbType) || "float".equalsIgnoreCase(dbType)) {
			return "java.lang.Double";
		} else if ("Clob".equalsIgnoreCase(dbType)) {
			return "java.lang.Clob";
		} else if ("Blob".equalsIgnoreCase(dbType)) {
			return "java.lang.Blob";
		} else if ("Date".equalsIgnoreCase(dbType)) {
			return "java.util.Date";
		}else if ("bit".equalsIgnoreCase(dbType)) {
			return "java.sql.Boolean";
		}
		return null;
	}

	@Override
	public String javaTypeToDbType(String javaType) {
		return null;
	}

}
