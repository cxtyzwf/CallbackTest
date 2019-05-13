package com.example.administrator.callbacktest.entity;

public class SingleEvent {
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
    public SingleEvent(String msg){
        this.msg = msg;
    }
}
