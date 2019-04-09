package com.jwt.demo;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Client {

	public static void main(String[] args) {
		String requestParamStr = "{\"body\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcmRlckNvZGUiOjE1NTQ3OTk3MTExMDYsIm9yZGVyQW1vdW50IjoxMjkuMDkwMDAwMDAwMDAwMDAzNDEwNjA1MTMxNjQ4NDgwODkyMTgxMzk2NDg0Mzc1LCJ1c2VyTmFtZSI6IuW8oOS4iSIsImhvYmJpZXMiOlt7Im5hbWUiOiLlj7DnkIMiLCJhZ2UiOjIyfSx7Im5hbWUiOiLnr67nkIMiLCJhZ2UiOjMwfSx7Im5hbWUiOiLmuLjms7MiLCJhZ2UiOjEyfV0sInVzZXJJZCI6Ijk0ODc1NyJ9.SmNLi-afwkIZ8zp2pZxATXQX7yVeI9eM16cLRFVwlFo\",\"channelCode\":\"CLTX\",\"digitalSign\":\"eeaa630316e2997be780e05f5cf28995\",\"timestamp\":\"1554799712\"}";
		System.out.println("request:" + requestParamStr);
		APIRequest requestParam = JSONObject.parseObject(requestParamStr, APIRequest.class);
		if (!requestParam.validatorSign(APIConst.SECRET)) {
			throw new RuntimeException("Ç©Ãû´íÎó£¡");
		}
		Map<String, Object> map = EncryptionUtil.decodeJWTPackage((String) requestParam.getBody(), APIConst.SECRET);
		System.out.println(JSONObject.toJSONString(map));
	}

}
