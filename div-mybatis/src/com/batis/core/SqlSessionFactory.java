package com.batis.core;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.batis.bean.Mapper;
import com.batis.bean.OpeateTag;
import com.batis.bean.Result;
import com.batis.bean.ResultMap;
import com.batis.bean.Sql;
public class SqlSessionFactory {

	private static Map<String,Mapper> mapperMaps=null;

	public SqlSessionFactory(Map<String, Mapper> mapperMaps) {
		SqlSessionFactory.mapperMaps = mapperMaps;
	}

	public SqlSession openSession() {
		Connection connection=DBMananger.getConnection();
		return new SqlSession(connection,SqlSessionFactory.mapperMaps);
	}

	@SuppressWarnings("unchecked")
	public static SqlSessionFactory build(InputStream inputStream) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			mapperMaps=new HashMap<String, Mapper>();
			Mapper mapper=new Mapper();
			Element root = document.getRootElement();
			String  namespace= root.attributeValue("namespace");
			mapper.setNamespace(namespace);
			mapperMaps.put(namespace, mapper);
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
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return new SqlSessionFactory(mapperMaps);
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
				Pattern p = Pattern.compile("\\{[^\\}]*\\}");
				Matcher m = p.matcher(sqlContent);
				LinkedHashMap<String,String> paramTypes=new LinkedHashMap<>();
				while (m.find()) {
					String item=sqlContent.substring(m.start(), m.end());
					String newItem=item.substring(1, item.length()-1);
					String[] array=newItem.split(",");
					String fieldNamee=array[0];
					String jdbcType=array[1].substring("jdbcType=".length());
					paramTypes.put(fieldNamee, jdbcType);
				}
				sql.setParamTypes(paramTypes);
				String psSql=sqlContent.replaceAll("\\{[^\\}]*\\}", "?").replaceAll("#", "").replaceAll("   ", "");
				sql.setPsSql(psSql);
				opeateTag.setSql(sql);
				opeateTagMaps.put(opeateTag.getId(), opeateTag);
			}
		}
	}
}
