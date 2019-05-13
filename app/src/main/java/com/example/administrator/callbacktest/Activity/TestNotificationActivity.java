package com.example.administrator.callbacktest.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.callbacktest.R;

import butterknife.BindView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestNotificationActivity extends AppCompatActivity {


    @BindView(R.id.simple)
    Button simple;
    @BindView(R.id.back)
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bga);
        ButterKnife.bind(this);


    }




    @OnClick({R.id.simple, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.simple:
                //获取NotificationManager实例
                NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //实例化NotificationCompat.Builde并设置相关属性
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        //设置小图标
                        .setSmallIcon(R.mipmap.calendar1)

                        //设置通知标题
                        .setContentTitle("最简单的Notification")
                        //设置通知内容
                        .setContentText("只有小图标、标题、内容");
                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                //.setWhen(System.currentTimeMillis());
                //通过builder.build()方法生成Notification对象,并发送通知,id=1
                notifyManager.notify(1, builder.build());
                break;
            case R.id.back:
                //获取PendingIntent
                Intent mainIntent = new Intent(this, TestNotificationActivity.class);
                PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //获取NotificationManager实例
                NotificationManager notifyManager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //创建 Notification.Builder 对象
                NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        //点击通知后自动清除
                        .setAutoCancel(true)
                        .setContentTitle("我是带Action的Notification")
                        .setContentText("点我会打开MainActivity")
                        .setContentIntent(mainPendingIntent);
                //发送通知
                notifyManager2.notify(2, builder2.build());
                break;
        }
    }
}
