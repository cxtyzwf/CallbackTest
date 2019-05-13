package com.example.administrator.callbacktest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.callbacktest.R;
import com.example.administrator.callbacktest.adapter.TestStickyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityC extends AppCompatActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TestStickyRecyclerViewAdapter adapter;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        ButterKnife.bind(this);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = getList(45);
        adapter = new TestStickyRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new StickyDecoration() {
            @Override
            public String getStickyHeaderName(int position) {
                return list.get(position);
            }
        });
        adapter.setList(list);
    }
    private List<String> getList(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            if (i < size / 3) {
                list.add("力量英雄");
            } else if (i < size / 3 * 2) {
                list.add("敏捷英雄");
            } else {
                list.add("智力英雄");
            }
        }
        return list;
    }

}
