package com.jwt.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;

public class Server {

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderCode", System.currentTimeMillis());
		map.put("orderAmount", new BigDecimal(129.09));
		map.put("userId", "948757");
		map.put("userName", "����");
		map.put("hobbies", Arrays.asList(new Hobby("̨��", 22), new Hobby("����", 30), new Hobby("��Ӿ", 12)));
		JWTSigner signer = new JWTSigner(APIConst.SECRET);
		String jwt = signer.sign(map);
		APIRequest request = new APIRequest();
		request.setChannelCode(APIConst.CHANNEL);
		request.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000L));
		request.setBody(jwt);
		request.setDigitalSign(request.buildDigitalSign(APIConst.SECRET));
		System.out.println(JSONObject.toJSONString(request));
	}

}
