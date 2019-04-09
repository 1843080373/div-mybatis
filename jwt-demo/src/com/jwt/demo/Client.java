package com.jwt.demo;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Client {

	public static void main(String[] args) {
		String requestParamStr = "{\"body\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcmRlckNvZGUiOjE1NTQ3OTA3MDU4NjQsIm9yZGVyQW1vdW50IjoiOTg4LjUwIiwidXNlcklkIjoiOTQ4NzU3In0._aFTIKPBDltkuNfBTUoLbyA73xTAJDDjvNS9w264DcI\",\"channelCode\":\"CLTX\",\"digitalSign\":\"32eb5c1b366bca6396c55ac62295a85f\",\"timestamp\":\"1554790708\"}";
		System.out.println("request:" + requestParamStr);
		APIRequest requestParam = JSONObject.parseObject(requestParamStr, APIRequest.class);
		if (!requestParam.validatorSign(APIConst.SECRET)) {
			throw new RuntimeException("Ç©Ãû´íÎó£¡");
		}
		Map<String, Object> map = EncryptionUtil.decodeJWTPackage((String) requestParam.getBody(), APIConst.SECRET);
		System.out.println(JSONObject.toJSONString(map));
	}

}
