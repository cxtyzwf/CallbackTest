package com.example.administrator.callbacktest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.callbacktest.R;
import com.example.administrator.callbacktest.entity.SingleEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityB extends AppCompatActivity {

    @BindView(R.id.publish)
    Button publish;
    @BindView(R.id.publish_sticky)
    Button publishSticky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        ButterKnife.bind(this);
        publish.setText("发布一个普通事件");
        publishSticky.setText("发布一个粘性事件");
    }

    @OnClick({R.id.publish, R.id.publish_sticky})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.publish:
                EventBus.getDefault().post(new SingleEvent("普通事件内容已从第二个Activity中获取到"));
                finish();
                break;
            case R.id.publish_sticky:
                EventBus.getDefault().postSticky(new SingleEvent("粘性事件内容已从第二个Activity中获取到"));
                finish();
                break;
        }
    }
}
