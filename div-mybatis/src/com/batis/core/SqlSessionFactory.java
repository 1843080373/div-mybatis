package com.batis.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.batis.bean.Environment;
import com.batis.bean.If;
import com.batis.bean.Mapper;
import com.batis.bean.MybatisCfg;
import com.batis.bean.MybatisDataSource;
import com.batis.bean.OpeateTag;
import com.batis.bean.Result;
import com.batis.bean.ResultMap;
import com.batis.bean.Sql;
import com.batis.utils.StringUtils;
public class SqlSessionFactory {

	private static MybatisCfg mybatisCfg=new MybatisCfg();
	
	private static Map<String,Mapper> mapperMaps=null;

	public SqlSessionFactory(Map<String, Mapper> mapperMaps) {
		SqlSessionFactory.mapperMaps = mybatisCfg.getMappers();
	}

	public SqlSession openSession() {
		Connection connection=DBMananger.getConnection(mybatisCfg);
		return new SqlSession(connection,mybatisCfg.getMappers());
	}

	@SuppressWarnings("unchecked")
	public static SqlSessionFactory build(InputStream inputStream) {
		try {
			
			Properties properties=new Properties();
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			//properties
			List<Element> propertiesElements = root.elements("properties");
			if(!propertiesElements.isEmpty()) {
				mybatisCfg.setProperties(propertiesElements.get(0).attributeValue("resource"));
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(mybatisCfg.getProperties()));
			}
			//defaultEnv
			List<Element> environmentsElements = root.elements("environments");
			if(!environmentsElements.isEmpty()) {
				mybatisCfg.setDefaultEnv(environmentsElements.get(0).attributeValue("default"));
				//environments
				Map<String,Environment> environments=new HashMap<String,Environment>();
				List<Element> envIntemsElements = environmentsElements.get(0).elements("environment");
				for (Element element : envIntemsElements) {
					Environment env=new Environment();
					env.setId(element.attributeValue("id"));
					env.setTransactionManagerType(element.element("transactionManager").attributeValue("type"));
					MybatisDataSource mybatisDataSource=new MybatisDataSource();
					Element dataSourceElement = element.element("dataSource");
					mybatisDataSource.setType(dataSourceElement.attributeValue("type"));
					List<Element> dsElement = dataSourceElement.elements("property");
					LinkedHashMap<String,String> dataSMap=new LinkedHashMap<>();
					for (Element ele : dsElement) {
						dataSMap.put(ele.attributeValue("name"), ele.attributeValue("value"));
					}
					mybatisDataSource.setUsername(properties.getProperty(StringUtils.get$StrValue(dataSMap.get("username"))));
					mybatisDataSource.setPassword(properties.getProperty(StringUtils.get$StrValue(dataSMap.get("password"))));
					mybatisDataSource.setDriver(properties.getProperty(StringUtils.get$StrValue(dataSMap.get("driver"))));
					mybatisDataSource.setUrl(properties.getProperty(StringUtils.get$StrValue(dataSMap.get("url"))));
					env.setMybatisDataSource(mybatisDataSource);
					environments.put(env.getId(), env);
				}
				mybatisCfg.setEnvironments(environments);
			}
			//mappers
			List<Element> mappersElements = root.element("mappers").elements("mapper");
			if(!mappersElements.isEmpty()) {
				Map<String,Mapper> mappers=new HashMap<>();
				for (Element element : mappersElements) {
					String resource = element.attributeValue("resource");
					mappers.put(StringUtils.str2Package(resource), null);
					buildMappers(mappers,new FileInputStream(new File("src/"+resource)));
				}
				mybatisCfg.setMappers(mappers);
				System.out.println("------->mappers="+JSONObject.toJSONString(mappers));
			}
			return new SqlSessionFactory(mapperMaps);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static void buildMappers(Map<String,Mapper> mappers,InputStream inputStream) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			mapperMaps=new HashMap<String, Mapper>();
			Mapper mapper=new Mapper();
			Element root = document.getRootElement();
			String  namespace= root.attributeValue("namespace");
			mapper.setNamespace(namespace);
			//resultMap
			List<Element> resultMapChildElements = root.elements("resultMap");
			if(!resultMapChildElements.isEmpty()) {
				Map<String, ResultMap> resultMaps=new HashMap<>();
				for (Element child : resultMapChildElements) {
					ResultMap resultMap=new ResultMap();
					resultMap.setId(child.attributeValue("id"));
					resultMap.setType(child.attributeValue("type"));
					List<Element> resultMapResultChildElements = child.elements("result");
					if(!resultMapResultChildElements.isEmpty()) {
						List<Result> results=new ArrayList<Result>();
						for (Element element : resultMapResultChildElements) {
							Result result=new Result(
									element.attributeValue("column"),
									element.attributeValue("property"),
									element.attributeValue("jdbcType"));
							results.add(result);
						}
						resultMap.setResults(results);
					}
					resultMaps.put(resultMap.getId(), resultMap);
				}
				mapper.setResultMaps(resultMaps);
			}
			
			//opeateTagMaps
			Map<String,OpeateTag> opeateTagMaps=new HashMap<>();
			//insert-delete-update-select
			buildOpreateData(root,"insert",opeateTagMaps);
			buildOpreateData(root,"update",opeateTagMaps);
			buildOpreateData(root,"delete",opeateTagMaps);
			buildOpreateData(root,"select",opeateTagMaps);
			mapper.setOpeateTagMaps(opeateTagMaps);
			mappers.put(namespace, mapper);
			mybatisCfg.setMappers(mapperMaps);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void buildOpreateData(Element root,String elementsName,Map<String,OpeateTag> opeateTagMaps) {
		List<Element> elements = root.elements(elementsName);
		if(!elements.isEmpty()) {
			for (Element child : elements) {
				OpeateTag opeateTag=new OpeateTag();
				opeateTag.setOpeate(child.getName());
				opeateTag.setId(child.attributeValue("id"));
				opeateTag.setParameterType(child.attributeValue("parameterType"));
				opeateTag.setResultMap(child.attributeValue("resultMap"));
				String sqlContent=child.getTextTrim();
				Sql sql=new Sql();
				sql.setSqlContent(sqlContent);
				opeateTag.setSql(sql);
				bulidOtherTags(opeateTag,child);
				opeateTagMaps.put(opeateTag.getId(), opeateTag);
			}
		}
	}

	/**
	 * ¶¯Ì¬±êÇ©
	 * @param opeateTag
	 * @param child
	 */
	@SuppressWarnings("unchecked")
	private static void bulidOtherTags(OpeateTag opeateTag, Element child) {
		List<Element> ifElements=child.elements("if");
		if(!ifElements.isEmpty()) {
			List<If> ifList=new ArrayList<>();
			for (Element element : ifElements) {
				If ifTag=new If();
				ifTag.setTest(element.attributeValue("test"));
				ifTag.setContext(element.getTextTrim());
				ifList.add(ifTag);
			}
			opeateTag.setIfList(ifList);
		}
	}
}
