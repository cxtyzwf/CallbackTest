package com.example.administrator.callbacktest.adapter;

import android.support.v7.widget.RecyclerView;

import com.example.administrator.callbacktest.entity.DataRecyclerView;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

public class BgaTestAdapter extends BGARecyclerViewAdapter<DataRecyclerView> {
    public BgaTestAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DataRecyclerView model) {

    }
}
