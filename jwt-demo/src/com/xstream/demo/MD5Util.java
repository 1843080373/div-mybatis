package com.xstream.demo;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static final String HEX_CHARS = "0123456789abcdef";

    private MD5Util() {}
    /**
     * 返回 MessageDigest MD5
     */
   private static MessageDigest getDigest() {
	   try {
           return MessageDigest.getInstance("MD5");
       } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException(e);
       }
    }
    /**
     * MD5加密，并返回作为一个十六进制字节
     */
    public static byte[] md5(byte[] data) {
        return getDigest().digest(data);
    }
    /**
     * MD5加密，并返回作为�?个十六进制字�?
     * <code>byte[]</code>.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data) {
    	byte[] bytes = null;
        try {
        	bytes = md5(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			
		}
		return bytes;
    }

    /**
     * MD5加密，并返回�?�?32字符的十六进制�??
     */
    public static String md5Hex(String data) {
        return toHexString(md5(data));
    }
 
    private static String toHexString(byte[] b) {
        StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
        	stringbuffer.append(HEX_CHARS.charAt(b[i] >>> 4 & 0x0F));
        	stringbuffer.append(HEX_CHARS.charAt(b[i] & 0x0F));
        }
        return stringbuffer.toString();
    }
}
