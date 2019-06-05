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
    private List<String> stickyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        ButterKnife.bind(this);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list = getList(20);
        adapter = new TestStickyRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);

        recyclerView.addItemDecoration(new StickyDecoration() {
            @Override
            public String getStickyHeaderName(int position) {
                return list.get(position);
            }
        });

    }
    private List<String> getList(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i < size / 2) {
                list.add("我的任务");
            } else {
                list.add("日程待办");
            }
        }
        return list;
    }

}
