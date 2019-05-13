package com.example.administrator.callbacktest.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result<T> {
//没有@Expose注解的不会被序列化，serialize为false代表不序列化， deserialize则代表反序列化：
    //使用注解自定义序列化字段名称，直接用Gson就可以，不用GsonBuilder生成对象

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}