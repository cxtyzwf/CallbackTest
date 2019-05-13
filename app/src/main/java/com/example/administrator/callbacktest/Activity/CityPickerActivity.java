package com.example.administrator.callbacktest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.callbacktest.R;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityPickerActivity extends AppCompatActivity {
    CityPickerView mPicker = new CityPickerView();
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicker.init(this);
        setContentView(R.layout.activity_city_picker);
        ButterKnife.bind(this);

    }



    @OnClick(R.id.button)
    public void onViewClicked() {
        CityConfig cityConfig = new CityConfig.Builder()
                .title(" ")//标题
                .titleTextSize(16)//标题文字大小
                .titleTextColor("#2999FB")//标题文字颜  色
                .titleBackgroundColor("#FFFFFF")//标题栏背景色
                .confirTextColor("#2999FB")//确认按钮文字颜色
                .confirmText("完成")//确认按钮文字
                .confirmTextSize(16)//确认按钮文字大小
                .cancelTextColor("#2999FB")//取消按钮文字颜色
                .cancelText("取消")//取消按钮文字
                .cancelTextSize(16)//取消按钮文字大小
                .setCityWheelType(CityConfig.WheelType.PRO_CITY)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .showBackground(true)//是否显示半透明背景
                .visibleItemsCount(5)//显示item的数量
                .province("浙江省")//默认显示的省份
                .city("宁波市")//默认显示省份下面的城市
                //                        .district("滨江区")//默认显示省市下面的区县数据
                .provinceCyclic(false)//省份滚轮是否可以循环滚动
                .cityCyclic(false)//城市滚轮是否可以循环滚动
                //                        .districtCyclic(false)//区县滚轮是否循环滚动
                .drawShadows(true)//滚轮显示模糊效果
                .setLineColor("#00ffffff")//中间横线的颜色
                .setLineHeigh(5)//中间横线的高度
                .setShowGAT(false)//是否显示港澳台数据，默认不显示
                .build();
//                CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        mPicker.showCityPicker();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
