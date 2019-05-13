package com.example.administrator.callbacktest;


import com.example.administrator.callbacktest.entity.Config;
import com.example.administrator.callbacktest.entity.Result;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IntentService {
    /**
     * 获取系统全部配置
     */
    @GET("/v1/system/settings")
    Call<Result<Config>> system();
}
