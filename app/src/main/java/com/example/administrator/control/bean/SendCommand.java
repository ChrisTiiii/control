package com.example.administrator.control.bean;


/**
 * author: ZhongMing
 * DATE: 2019/1/14 0014
 * Description:
 **/
public class SendCommand {
    private String From;
    private String SendTo;
    private String Time;
    private String Type;
    private String Status;
    private Command Command;
    private String Msg;

    public SendCommand(String from, String sendTo, String time, String type, String status, SendCommand.Command command, String msg) {
        From = from;
        SendTo = sendTo;
        Time = time;
        Type = type;
        Status = status;
        Command = command;
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

    public SendCommand.Command getCommand() {
        return Command;
    }

    public void setCommand(SendCommand.Command command) {
        Command = command;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public static class Command {
        private Integer Kind;
        private Integer Order;

        public Command(Integer kind, Integer order) {
            Kind = kind;
            Order = order;
        }

        public Integer getKind() {
            return Kind;
        }

        public void setKind(Integer kind) {
            Kind = kind;
        }

        public Integer getOrder() {
            return Order;
        }

        public void setOrder(Integer order) {
            Order = order;
        }
    }
}
