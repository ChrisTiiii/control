package com.example.administrator.control.bean;

/**
 * author: ZhongMing
 * DATE: 2019/1/14 0014
 * Description:连接那台电脑
 **/
public class Connect {
    private String From;//自己的id
    private String Type;//这个写这个字符串
    private String SendTo;//发你要连接的电脑
    private String Msg;

    public Connect(String from, String type, String sendTo, String msg) {
        From = from;
        Type = type;
        SendTo = sendTo;
        Msg = msg;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String sendTo) {
        SendTo = sendTo;
    }
}
