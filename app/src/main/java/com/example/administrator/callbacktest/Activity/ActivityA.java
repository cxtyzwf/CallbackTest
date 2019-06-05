package com.example.administrator.callbacktest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.administrator.callbacktest.IntentService;
import com.example.administrator.callbacktest.R;
import com.example.administrator.callbacktest.entity.Company;
import com.example.administrator.callbacktest.entity.Config;
import com.example.administrator.callbacktest.entity.Result;
import com.example.administrator.callbacktest.entity.SingleEvent;
import com.example.administrator.callbacktest.fragment.Fragment1;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityA extends AppCompatActivity {

    @BindView(R.id.event)
    Button event;
    @BindView(R.id.event_sticky)
    Button event_sticky;
    @BindView(R.id.show_text)
    TextView show_text;
    @BindView(R.id.show_image)
    ImageView show_image;
    @BindView(R.id.image_request)
    Button image_request;
    @BindView(R.id.intent_request)
    Button intentRequest;
    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    @BindView(R.id.intent_recyclerview)
    Button intent_recyclerview;
    @BindView(R.id.view_pager)
    Button view_pager;
    @BindView(R.id.bga_refresh)
    Button bga_refresh;
    @BindView(R.id.time)
    Button time;
    @BindView(R.id.citypicker)
    Button citypicker;
    @BindView(R.id.calendar)
    Button calendar;
    @BindView(R.id.word)
    Button word;
    @BindView(R.id.panel)
    Button panel;

    private String intentUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538194830461&di=2fbe352a7527b4dd04c892d50371a72e&imgtype=0&src=http%3A%2F%2Fhimg2.huanqiu.com%2Fattachment2010%2F2015%2F1010%2F20151010093213972.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Fragment1(), null).commit();


/** -----------------------------Gson测试-------------------------- */
//json转换bean
        Gson gson = new Gson();
        String json = "{\"id\":\"1\",\"name\":\"geenk\"}";
        Company c = gson.fromJson(json, Company.class);
        Log.d("Activity-a", c.toString());

//bean转换json

//        Company company = new Company("3","jack");
        String s = gson.toJson(new Company("3", "jack"));
        Log.d("Activity-a", s);


        String json2 = "[{\"id\":\"1\",\"name\":\"geenk\"},{\"id\":\"2\",\"name\":\"alibaba\"}]";

//json转换成List
        List list = gson.fromJson(json2, new TypeToken<List>() {
        }.getType());
        Log.d("Activity-a", list.toString());

//json转换成Set
        Set set = gson.fromJson(json2, new TypeToken<Set>() {
        }.getType());
        Log.d("Activity-a", set.toString());

/** -----------------------------Fastjson测试------------------------- */

//格式化json
        String s2 = JSON.toJSONString(new Company("3", "jack"), true);
        Log.d("Activity-a", s2);

//非格式化的json
        String s3 = JSON.toJSONString(new Company("3", "jack"), false);
        Log.d("Activity-a", s3);


//json转换bean
        Company company2 = JSON.parseObject(json, Company.class);
        Log.d("Activity-a", company2.toString());

//将json转换成List
        List list2 = JSON.parseObject(json2, new TypeReference<ArrayList>() {
        });
        Log.d("Activity-a", list2.toString());

//将json转换成Set
        Set set2 = JSON.parseObject(json2, new TypeReference<HashSet>() {
        });
        Log.d("Activity-a", set2.toString());

/**--------------------------------------------------------------------*/
/** -----------------------------Fastjson与Gson的区别------------------------- */
        //一个bean类转化成json，如果有get函数，但没有其对应的属性，Gson是不会有这个属性的，但是Fastjson会补充上去
        Company cm = new Company("123456", "libai");
        String d = gson.toJson(cm);
        Log.e("Activity-a,different", d);// {"id":"123456","name":"libai"}

        d = JSON.toJSONString(cm, false);
        Log.e("Activity-a,different", d);//  {"age":23,"id":"123456","name":"libai"}
    }

    //处理接收方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SingleEvent(SingleEvent singleEvent) {
        show_text.setText(singleEvent.getMsg());
    }


    @OnClick({R.id.event, R.id.event_sticky, R.id.image_request, R.id.intent_request, R.id.intent_recyclerview, R.id.view_pager,
            R.id.bga_refresh, R.id.time, R.id.citypicker, R.id.calendar, R.id.word,R.id.panel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.event:
                startActivity(new Intent(this, ActivityB.class));
                break;
            case R.id.event_sticky:
                startActivity(new Intent(this, ActivityB.class));
                break;
            case R.id.image_request:
                Picasso
                        .with(this)
                        .load(intentUrl)
                        .into(show_image);
                break;
            case R.id.intent_request:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://42.159.94.67:9090/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                IntentService intentService = retrofit.create(IntentService.class);
                Call<Result<Config>> call = intentService.system();
                call.enqueue(new Callback<Result<Config>>() {
                    @Override
                    public void onResponse(Call<Result<Config>> call, Response<Result<Config>> response) {
                        show_text.setText(response.body().getCode() + response.body().getMessage() + response.body().getData());
                    }

                    @Override
                    public void onFailure(Call<Result<Config>> call, Throwable t) {

                    }
                });
                break;
            case R.id.intent_recyclerview:
                startActivity(new Intent(this, ActivityC.class));
                break;
            case R.id.view_pager:
                startActivity(new Intent(this, ActivityD.class));
                break;
            case R.id.bga_refresh:
                startActivity(new Intent(this, TestNotificationActivity.class));
                break;
            case R.id.time:
                startActivity(new Intent(this, ActivityE.class));
                break;
            case R.id.citypicker:
                startActivity(new Intent(this, CityPickerActivity.class));
                break;
            case R.id.calendar:
                startActivity(new Intent(this, CalendarActivity.class));
                break;
            case R.id.word:

                break;
            case R.id.panel:
                startActivity(new Intent(this, PanelActivity.class));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }



}
