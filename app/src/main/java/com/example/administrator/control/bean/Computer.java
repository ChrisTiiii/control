package com.example.administrator.control.bean;

import java.util.List;

/**
 * author: ZhongMing
 * DATE: 2019/1/11 0011
 * Description:
 **/
public class Computer {

    /**
     * From : Server
     * SendTo : client
     * Time : Fri Jan 11 17:39:36 2019
     * Type : userlist
     * Status : Success
     * command : null
     * Msg : ["222222","111111"]
     */

    private String From;
    private String SendTo;
    private String Time;
    private String Type;
    private String Status;
    private Object command;
    private List<String> Msg;

    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String SendTo) {
        this.SendTo = SendTo;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Object getCommand() {
        return command;
    }

    public void setCommand(Object command) {
        this.command = command;
    }

    public List<String> getMsg() {
        return Msg;
    }

    public void setMsg(List<String> Msg) {
        this.Msg = Msg;
    }
}
