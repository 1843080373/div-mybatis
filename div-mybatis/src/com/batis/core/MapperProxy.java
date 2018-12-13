package com.batis.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.batis.bean.Mapper;
import com.batis.bean.OpeateTag;
import com.batis.bean.Result;
import com.batis.bean.ResultMap;
import com.batis.utils.ReflectUtils;

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
		Connection connection = sqlSession.getConnection();
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
		System.out.println("namespace  " + namespace);
		System.out.println("methodName  " + methodName);
		System.out.println("arg  "+arg1);
		String parameterType=opeateTag.getParameterType();
		if(parameterType!=null&&arg!=null&&!ReflectUtils.isBaseType(arg1)) {
			if (parameterType != null
					&& !parameterType.equals(arg1.getClass().getPackage().getName() + "." + arg1.getClass().getName())) {
				throw new RuntimeException("parameterType不匹配");
			}
		}
		PreparedStatement ps = connection.prepareStatement(opeateTag.getSql().getPsSql());
		buildParams(ps, opeateTag,arg1);
		System.out.println("sql  "+opeateTag.getSql().getPsSql());
		if (isDML(opeateTag.getOpeate())) {
			o = ps.executeUpdate();
		} else {
			if(!mapper.getResultMaps().keySet().contains(opeateTag.getResultMap())) {
				throw new RuntimeException("找不到resultMap");
			}
			ResultSet rs = ps.executeQuery();
			o=buildResultSet(mapper.getResultMaps().get(opeateTag.getResultMap()),opeateTag, rs);
		}
		ps.close();
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
	
	private void buildParams(PreparedStatement ps, OpeateTag opeateTag,Object arg1) {
		try {
			if(arg1!=null) {
				String parameterType = opeateTag.getParameterType();
				Class.forName(parameterType);
				Map<String,String> paramTypes=opeateTag.getSql().getParamTypes();
				//{"password": "VARCHAR"}
				int index=0;
				for (String fieldName : paramTypes.keySet()) {
					index++;
					if(ReflectUtils.isBaseType(arg1)) {
						ps.setObject(index, arg1);
					}else {
						Object fieldValue=ReflectUtils.invokeGet(arg1, fieldName);
						ps.setObject(index, fieldValue);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("parameterType classNotFound");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean isDML(String opreate) {
		return "insert".equals(opreate) || "update".equals(opreate) || "delete".equals(opreate);

	}

}
