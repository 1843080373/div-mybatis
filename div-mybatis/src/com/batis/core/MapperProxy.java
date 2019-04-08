package com.batis.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.batis.bean.Mapper;
import com.batis.bean.OpeateTag;
import com.batis.bean.Result;
import com.batis.bean.ResultMap;
import com.batis.bean.tag.If;
import com.batis.bean.tag.Sql;
import com.batis.utils.ReflectUtils;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

public class MapperProxy<T> implements InvocationHandler {

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@SuppressWarnings("unchecked")
	public T getInstance(Class<T> t) {
		return (T) Proxy.newProxyInstance(t.getClassLoader(), new Class<?>[] { t }, this);
	}

	@Override
	public Object invoke(Object obj, Method m, Object[] arg) throws Throwable {
		Object o = null;
		PreparedStatement ps=null;
		Connection connection =null;
		try {
			connection = sqlSession.getConnection();
			Map<String, Mapper> mapperMap = sqlSession.getMapperMaps();
			String namespace = obj.getClass().getInterfaces()[0].getName();
			Mapper mapper = mapperMap.get(namespace);
			if (mapper == null) {
				throw new RuntimeException("找不到mapper");
			}
			String methodName = m.getName();
			OpeateTag opeateTag = mapper.getOpeateTagMaps().get(methodName);
			if (opeateTag == null) {
				throw new RuntimeException("找不到opeateTag");
			}
			Object arg1 = null;
			if (arg != null && arg.length > 1) {
				throw new RuntimeException("arg参数太长");
			}
			if(arg!=null) {
				arg1=arg[0];
			}
			System.out.println("mapper\t" + namespace);
			System.out.println("method\t" + methodName);
			System.out.println("arg\t"+arg1);
			String parameterType=opeateTag.getParameterType();
			if(parameterType!=null&&arg!=null&&!ReflectUtils.isBaseType(arg1)&&!(arg1 instanceof Map)) {
				String argType=arg1.getClass().getName();
				if (parameterType != null
						&& !parameterType.equals(argType)) {
					throw new RuntimeException("parameterType不匹配");
				}
			}
			ps=buildParams(ps, opeateTag,arg1,connection);
			System.out.println("sql\t"+opeateTag.getSql().getPsSql());
			if (isDML(opeateTag.getOpeate())) {
				o = ps.executeUpdate();
			} else {
				if(!mapper.getResultMaps().keySet().contains(opeateTag.getResultMap())) {
					throw new RuntimeException("找不到resultMap");
				}
				ResultSet rs = ps.executeQuery();
				o=buildResultSet(mapper.getResultMaps().get(opeateTag.getResultMap()),opeateTag, rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		   DBMananger.closeConnection(connection, ps);
		}
		return o;
	}
	
	private Object buildResultSet(ResultMap resultMap,OpeateTag opeateTag,ResultSet rs) {
		List<Object> list=new ArrayList<>();
		try {
			TypeConvertor convertor=new MySQLTypeConvertor();
			while (rs.next()) {
				Object item=Class.forName(resultMap.getType()).newInstance();
				List<Result> results = resultMap.getResults();
				for (Result result : results) {
					 String column=result.getColumn();
					 String property=result.getProperty();
					 String jdbcType=result.getJdbcType();
					 ReflectUtils.invokeSet(item, property, convertor.dbTypeToJavaType(jdbcType), rs.getObject(column));
				}
				list.add(item);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	private PreparedStatement buildParams(PreparedStatement ps, OpeateTag opeateTag,Object arg1,Connection connection) {
		try {
			if(arg1!=null) {
				String parameterType = opeateTag.getParameterType();
				Class.forName(parameterType);
				Sql sql=opeateTag.getSql();
				String sqlContent=sql.getSqlContent();
				buildDynamicSQL(sql, sqlContent);
				if(opeateTag.getIfList()!=null&&opeateTag.getIfList().size()>0) {
					for (If IfTag : opeateTag.getIfList()) {
						boolean excuteTest=excuteTest(arg1,IfTag.getTest(),opeateTag.getAlias());
						if(excuteTest) {
							buildDynamicSQL(sql, IfTag.getContext(),sql.getParamTypes(),sql.getPsSql());
						}
					}
				}
				System.out.println(JSONObject.toJSONString(opeateTag.getSql()));
				Map<String,String> paramTypes=opeateTag.getSql().getParamTypes();
				ps = connection.prepareStatement(opeateTag.getSql().getPsSql());
				int index=0;
				for (String fieldName : paramTypes.keySet()) {
					index++;
					if(ReflectUtils.isBaseType(arg1)) {
						ps.setObject(index, arg1);
					}else {
						if(arg1 instanceof Map) {
							Map map=(Map) arg1;
							ps.setObject(index, map.get(fieldName));
						}else{
							Object fieldValue=ReflectUtils.invokeGet(arg1, fieldName);
							ps.setObject(index, fieldValue);
						}
					}
				}
			}else {
				ps = connection.prepareStatement(opeateTag.getSql().getPsSql());
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("parameterType classNotFound");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}

	private boolean excuteTest(Object arg1, String test,String alias) {
		if(ReflectUtils.isBaseType(arg1)) {
			Expression compiledExp = AviatorEvaluator.compile(test);
			Map<String, Object> env = new HashMap<String, Object>();
			env.put(alias, arg1);
			return (boolean) compiledExp.execute(env);
		}else {
			Map<String, Object> env = new HashMap<String, Object>();
			env.put(alias, arg1);
			return (boolean) AviatorEvaluator.execute(test, env);
		}
	}
	private void buildDynamicSQL(Sql sql, String sqlContent) {
		matcher(sql, sqlContent, null);
		String psSql=sqlContent.replaceAll("\\{[^\\}]*\\}", "?").replaceAll("#", "").replaceAll("   ", "");
		sql.setPsSql(psSql);
	}
	
	private void buildDynamicSQL(Sql sql, String sqlContent,LinkedHashMap<String,String> paramTypes,String psSql) {
		matcher(sql, sqlContent, paramTypes);
		String psSqlDynamic=sqlContent.replaceAll("\\{[^\\}]*\\}", "?").replaceAll("#", "").replaceAll("   ", "");
		sql.setPsSql(psSql+" "+psSqlDynamic);
	}

	private void matcher(Sql sql, String sqlContent, LinkedHashMap<String, String> paramTypes) {
		Pattern p = Pattern.compile("\\{[^\\}]*\\}");
		if(paramTypes==null) {
			paramTypes=new LinkedHashMap<>();
		}
		Matcher m = p.matcher(sqlContent);
		while (m.find()) {
			String item=sqlContent.substring(m.start(), m.end());
			String newItem=item.substring(1, item.length()-1);
			String[] array=newItem.split(",");
			String fieldName=array[0];
			String jdbcType=array[1].substring("jdbcType=".length());
			paramTypes.put(fieldName, jdbcType);
		}
		sql.setParamTypes(paramTypes);
	}

	private boolean isDML(String opreate) {
		return "insert".equals(opreate) || "update".equals(opreate) || "delete".equals(opreate);
	}

}
