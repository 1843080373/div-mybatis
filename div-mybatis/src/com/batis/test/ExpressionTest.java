package com.batis.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.batis.bean.ExpressionBean;
import com.batis.core.ExpersionTypes;

public class ExpressionTest {
	public static void main(String[] args) {
		Object arg1 = new HashMap<String, Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("userName", "ÕÅÈý");
				put("phone", "1807657569");
				put("weigth", "180");
				put("sex", "M");
			}
		};
		HashMap<String,ExpressionBean> ExpressionBeanMap=new LinkedHashMap<String,ExpressionBean>();
		String test = "userName neq null and phone eq '1807657569' or weigth gt 180 and sex eq 'M'";
		String[] strs = test.split(ExpersionTypes.and);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			ExpressionBean expressionBean=new ExpressionBean();
			String strItem=strs[i].trim();
			System.out.println("and--"+strItem);
			if(strItem.contains(ExpersionTypes.eq)){
				String[] expersion=strItem.split(ExpersionTypes.eq);
				expressionBean.setFeild(expersion[0].trim());
				expressionBean.setExpression(ExpersionTypes.eq);
				expressionBean.setValue(expersion[1].trim());
			}else if(strItem.contains(ExpersionTypes.neq)){
				String[] expersion=strItem.split(ExpersionTypes.neq);
				expressionBean.setFeild(expersion[0].trim());
				expressionBean.setExpression(ExpersionTypes.neq);
				expressionBean.setValue(expersion[1].trim());
			}else if(strItem.contains(ExpersionTypes.gt)){
				String[] expersion=strItem.split(ExpersionTypes.gt);
				expressionBean.setFeild(expersion[0].trim());
				expressionBean.setExpression(ExpersionTypes.gt);
				expressionBean.setValue(expersion[1].trim());
			}else if(strItem.contains(ExpersionTypes.lt)){
				String[] expersion=strItem.split(ExpersionTypes.lt);
				expressionBean.setFeild(expersion[0].trim());
				expressionBean.setExpression(ExpersionTypes.lt);
				expressionBean.setValue(expersion[1].trim());
			}
			ExpressionBeanMap.put(ExpersionTypes.and,expressionBean);
			String[] strs2 = strItem.split(ExpersionTypes.or);
			for (int j = 0; j < strs2.length; j++) {
				ExpressionBean expressionBean1=new ExpressionBean();
				String strItemOr=strs2[j].trim();
				System.out.println("or--"+strItemOr);
				if(strItemOr.contains(ExpersionTypes.eq)){
					String[] expersion=strItem.split(ExpersionTypes.eq);
					expressionBean1.setFeild(expersion[0].trim());
					expressionBean1.setExpression(ExpersionTypes.eq);
					expressionBean1.setValue(expersion[1].trim());
				}else if(strItemOr.contains(ExpersionTypes.neq)){
					String[] expersion=strItem.split(ExpersionTypes.neq);
					expressionBean1.setFeild(expersion[0].trim());
					expressionBean1.setExpression(ExpersionTypes.neq);
					expressionBean1.setValue(expersion[1].trim());
				}else if(strItemOr.contains(ExpersionTypes.gt)){
					String[] expersion=strItem.split(ExpersionTypes.gt);
					expressionBean1.setFeild(expersion[0].trim());
					expressionBean1.setExpression(ExpersionTypes.gt);
					expressionBean1.setValue(expersion[1].trim());
				}else if(strItemOr.contains(ExpersionTypes.lt)){
					String[] expersion=strItem.split(ExpersionTypes.lt);
					expressionBean1.setFeild(expersion[0].trim());
					expressionBean1.setExpression(ExpersionTypes.lt);
					expressionBean1.setValue(expersion[1].trim());
				}
				ExpressionBeanMap.put(ExpersionTypes.or,expressionBean1);
			}
			list.addAll(Arrays.asList(strs2));
		}
		Object[] c = list.toArray();
		String[] strs_01=new String[c.length];
        for (int i = 0; i < c.length; i++) {
        	strs_01[i]=c[i].toString().trim();
        	//System.out.println(strs_01[i]);
		}
        
        System.out.println(JSONObject.toJSONString(ExpressionBeanMap));
	}
}
