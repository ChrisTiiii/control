package com.example.administrator.control.bean;


public class EqupmentBean {
    private String name;
    private int status;//-1为断开，1为连接

    public EqupmentBean(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
