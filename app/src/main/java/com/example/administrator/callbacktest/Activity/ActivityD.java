package com.example.administrator.callbacktest.Activity;

import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.callbacktest.fragment.Fragment1;
import com.example.administrator.callbacktest.adapter.MyViewPagerAdapter;
import com.example.administrator.callbacktest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityD extends AppCompatActivity {

    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.yuandian_layout)
    LinearLayout yuandian_layout;



    private List<Fragment> list;
    private String[] titles={"页面1","页面2","页面3","页面4","页面5","页面6"};
    private MyViewPagerAdapter myViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        ButterKnife.bind(this);

        list=new ArrayList<>();
        list.add(new Fragment1());
        list.add(new Fragment1());
        list.add(new Fragment1());
        list.add(new Fragment1());
        list.add(new Fragment1());
        list.add(new Fragment1());

        inityuandian();

        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),this,list,titles);

        viewPager.setAdapter(myViewPagerAdapter);
       

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            for (int x =0;x<list.size();x++){
                if (x == i){
                    yuandian_layout.getChildAt(x).setSelected(true);
                }else {
                    yuandian_layout.getChildAt(x).setSelected(false);
                }
            }

            }

            @Override
            public void onPageScrollStateChanged(int i) {


            }
        });

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //将TabLayout和ViewPager绑定在一起，一个动另一个也会跟着动
        tabLayout.setupWithViewPager(viewPager);


    }

    private void inityuandian() {

        ImageView imageView;
        for (int i= 0;i<list.size();i++){
            //创建底部指示器(小圆点)

            imageView = new ImageView(ActivityD.this);
            imageView.setImageResource(R.drawable.vp_picture_change);
            imageView.setEnabled(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER);

            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,12);
            //设置间隔
                layoutParams.leftMargin = 14;
            //添加到LinearLayout
            yuandian_layout.addView(imageView, layoutParams);
            yuandian_layout.getChildAt(0).setSelected(true);

        }
    }


}
