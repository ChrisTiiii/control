package com.example.administrator.control.bean;


/**
 * author: ZhongMing
 * DATE: 2019/1/14 0014
 * Description:
 **/
public class AcceptCommand {

    /**
     * From : Server
     * SendTo : client
     * Time : Mon Jan 14 14:29:49 2019
     * Type : userlist
     * Status : Success
     * SendCommand : null
     * Msg : ["111111","222222","333333","444444","555555","666666","33333"]
     */

    private String From;
    private String SendTo;
    private String Time;
    private String Type;
    private String Status;
    private Object Command;
    private Object Msg;

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
        return Command;
    }

    public void setCommand(Object Command) {
        this.Command = Command;
    }

    public Object getMsg() {
        return Msg;
    }

    public void setMsg(Object msg) {
        Msg = msg;
    }
}
