package com.jwt.demo;


public class APIRequest {


    private String channelCode;


    /**
     * 请求时间
     */
    private String timestamp;

    /**
     * 数字签名
     * 当body不为空时
     ^channelCode&secret&timestamp*加密body后得到的密文
     当body为空
     ^channelCode&secret&timestamp*

     */
    private String digitalSign;

    /**
     * 涉及具体业务API的请求参
     * api请求参数进行jwt加密
     *
     */
    private Object body;

    public String buildDigitalSign(String secret) {
        String str = "^" + channelCode + "&" + secret + "&" + timestamp + "*";
        if(body != null) {
            str += body;
        }
        return EncryptionUtil.md5(str);
    }

    public boolean validatorSign(String secret) {

        return this.buildDigitalSign(secret).equals(digitalSign);
    }



    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDigitalSign() {
        return digitalSign;
    }

    public void setDigitalSign(String digitalSign) {
        this.digitalSign = digitalSign;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
