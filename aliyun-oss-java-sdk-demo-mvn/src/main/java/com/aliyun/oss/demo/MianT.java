package com.aliyun.oss.demo;

import java.io.InputStream;

public class MianT {

	public static void main(String[] args) {
		OSSClientUtil OSSClientUtil = new OSSClientUtil();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("MySQL新技术在淘宝的使用.pdf");
		String name = OSSClientUtil.uploadImg2Oss(is, "MySQL新技术在淘宝的使用.pdf");
		System.out.println(name);
		String imgUrl = OSSClientUtil.getImgUrl(name);
		System.out.println(imgUrl);
	}

}
