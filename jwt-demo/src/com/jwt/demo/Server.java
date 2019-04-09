package com.jwt.demo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;

public class Server {

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderCode", System.currentTimeMillis());
        map.put("orderAmount", "988.50");
        map.put("userId", "948757");
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
