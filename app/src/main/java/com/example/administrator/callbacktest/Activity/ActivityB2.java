package com.example.administrator.callbacktest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.callbacktest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityB2 extends AppCompatActivity {


    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b2);
        ButterKnife.bind(this);


    }
}
