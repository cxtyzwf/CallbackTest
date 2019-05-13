package com.example.administrator.callbacktest.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String[] titles;
    private Context mcontext;
    public MyViewPagerAdapter(FragmentManager fm,Context mcontext ,List<Fragment> list,String[] titles) {
        super(fm);
        this.list = list;
        this.titles =titles;
        this.mcontext = mcontext;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}
