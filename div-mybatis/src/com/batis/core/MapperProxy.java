package com.batis.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.batis.bean.Mapper;
import com.batis.bean.OpeateTag;
import com.batis.bean.Result;
import com.batis.bean.ResultMap;
import com.batis.bean.tag.Choose;
import com.batis.bean.tag.Foreach;
import com.batis.bean.tag.If;
import com.batis.bean.tag.Set;
import com.batis.bean.tag.Sql;
import com.batis.bean.tag.When;
import com.batis.bean.tag.Where;
import com.batis.utils.ReflectUtils;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

public class MapperProxy<T> implements InvocationHandler {

	private SqlSession sqlSession;

	private static final String WHERE_SQL=" WHERE 1=1 ";
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
				if(opeateTag.getWhere()!=null) {
					sqlContent+=WHERE_SQL+(opeateTag.getWhere().getPreSql());
				}
				sql.setSqlContent(sqlContent);
				buildALLSQL(opeateTag, arg1,sql);
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
	
	private void buildALLSQL(OpeateTag opeateTag, Object arg1, Sql sql) {
		buildDynamicSQL(sql, sql.getSqlContent());
		Where where=opeateTag.getWhere();
		if(where!=null) {
			buildSetSQL(opeateTag,opeateTag.getSet(), arg1, sql,opeateTag.getAlias());
			buildIfSQL(where.getIfList(),  arg1, sql,opeateTag.getAlias());
			buildChooseSQL(where.getChoose(), arg1, sql,opeateTag.getAlias());
			buildForeachSQL(where.getForeachList(), arg1, sql);
		}else {
			buildSetSQL(opeateTag,opeateTag.getSet(), arg1, sql,opeateTag.getAlias());
			buildIfSQL(opeateTag.getIfList(),  arg1, sql,opeateTag.getAlias());
			buildChooseSQL(opeateTag.getChoose(), arg1, sql,opeateTag.getAlias());
			buildForeachSQL(opeateTag.getForeachList(), arg1, sql);
		}
	}

	private void buildIfSQL(List<If> ifList, Object arg1, Sql sql,String alias) {
		if(ifList!=null&&ifList.size()>0) {
			for (If IfTag : ifList) {
				boolean excuteTest=excuteTest(arg1,IfTag.getTest(),alias);
				if(excuteTest) {
					buildDynamicSQL(sql, IfTag.getContext(),sql.getParamTypes(),sql.getPsSql());
				}
			}
		}
	}
	
	private void buildChooseSQL(Choose choose, Object arg1, Sql sql,String alias) {
		if(choose!=null) {
			LinkedList<When> whenLists=choose.getWhenLists();
			boolean excuteOtherwise=false;
			if(!whenLists.isEmpty()) {
				for (When when : whenLists) {
					boolean excuteTest=excuteTest(arg1,when.getTest(),alias);
					if(excuteTest) {
						buildDynamicSQL(sql, when.getContext(),sql.getParamTypes(),sql.getPsSql());
					}else {
						excuteOtherwise=true;
					}
				}
			}
			String otherwise=choose.getOtherwise();
			if(!"".equals(otherwise)&&excuteOtherwise) {
				buildDynamicSQL(sql, otherwise,sql.getParamTypes(),sql.getPsSql());
			}
		}
	}
	
	private void buildForeachSQL(LinkedList<Foreach> foreachList, Object arg1, Sql sql) {
		if(foreachList!=null&&foreachList.size()>0) {
			for (Foreach foreach : foreachList) {
				String item=foreach.getItem();
				String index=foreach.getIndex();
				String collection=foreach.getCollection();
				String open=foreach.getOpen();
				String separator=foreach.getSeparator();
				String close=foreach.getClose();
				String context=foreach.getContext();
				Object entity=null;
				if(arg1 instanceof Map) {
					Map map=(Map) arg1;
					entity=map.get(collection);
				}else{
					entity=ReflectUtils.invokeGet(arg1, collection);
				}
				if(entity!=null) {
					StringBuilder newPSsql=new StringBuilder(open);
					if(entity instanceof Map) {
						Map<?, ?> ent=(Map<?, ?>)entity;
						for (Entry<?, ?> entry : ent.entrySet()) {
							newPSsql.append("?,");
						}
					}else if(entity instanceof Collection) {
						Collection<?> ent=(Collection<?>)entity;
						for (Object o : ent) {
							newPSsql.append("?,");
						}
					}else if(entity instanceof java.util.Set) {
						java.util.Set<?> ent=(java.util.Set<?>)entity;
						for (Object o : ent) {
							newPSsql.append("?,");						
						}
					}else if(entity instanceof Object[]){
						Object[] ent=(Object[])entity;
						for (int i = 0; i < ent.length; i++) {
							newPSsql.append("?,");
						}
					}else {
						throw new RuntimeException("参数错误");
					}
					newPSsql=newPSsql.deleteCharAt(newPSsql.length()-1);
					newPSsql.append(close);
					sql.setPsSql(sql.getPsSql()+" "+newPSsql);
				}
			}
		}
	}
	
	private void buildSetSQL(OpeateTag opeateTag, Set set, Object arg1, Sql sql,String alias) {
		if(set!=null&&set.getIfList()!=null&&set.getIfList().size()>0) {
			Sql new_sql=sql;
			new_sql.setPsSql(new_sql.getPsSql()+" SET ");
			new_sql.setSqlContent(new_sql.getSqlContent()+" SET ");
			opeateTag.setSql(new_sql);
			for (If IfTag : set.getIfList()) {
				boolean excuteTest=excuteTest(arg1,IfTag.getTest(),alias);
				if(excuteTest) {
					buildDynamicSQL(sql, IfTag.getContext(),sql.getParamTypes(),sql.getPsSql());
				}
			}
			sql.setPsSql(new StringBuffer(sql.getPsSql()).deleteCharAt(sql.getPsSql().length()-1).toString());
		}
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
