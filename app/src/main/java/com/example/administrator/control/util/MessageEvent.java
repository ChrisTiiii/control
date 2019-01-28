package com.example.administrator.control.util;

/**
 * author: ZhongMing
 * DATE: 2019/1/11 0011
 * Description:
 **/
public class MessageEvent {
    private int TAG;
    private String message;

    public MessageEvent(int TAG) {
        this.TAG = TAG;
    }

    public int getTAG() {
        return TAG;
    }

    public void setTAG(int TAG) {
        this.TAG = TAG;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}