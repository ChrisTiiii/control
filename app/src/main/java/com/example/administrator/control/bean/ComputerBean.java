package com.example.administrator.control.bean;

/**
 * author: ZhongMing
 * DATE: 2019/1/11 0011
 * Description:
 **/
public class ComputerBean {
    private String From;
    private String SendTo;
    private String Time;
    private String Type;
    private String Status;
    private String command;
    private String Msg;

    public ComputerBean(String from, String sendTo, String time, String type, String status, String command, String msg) {
        From = from;
        SendTo = sendTo;
        Time = time;
        Type = type;
        Status = status;
        this.command = command;
        Msg = msg;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String sendTo) {
        SendTo = sendTo;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
