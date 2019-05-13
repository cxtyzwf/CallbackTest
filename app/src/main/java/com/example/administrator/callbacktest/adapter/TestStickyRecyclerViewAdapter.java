package com.example.administrator.callbacktest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.callbacktest.R;
import com.example.administrator.callbacktest.entity.DataRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestStickyRecyclerViewAdapter extends RecyclerView.Adapter<TestStickyRecyclerViewAdapter.MyVH> {



    public interface onItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private onItemClickListener monItemClickListener;

    public void setOnItemClickListener(onItemClickListener monItemClickListener) {
        this.monItemClickListener = monItemClickListener;
    }

    private Context mContext;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    private List<String> list = new ArrayList<>();

    public void clearList(){
        list.clear();
    }

    public TestStickyRecyclerViewAdapter(Context context) {

        mContext = context;

    }

    public class MyVH extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView textView;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.test_sticky_item_recyclerview, viewGroup, false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH myVH, int i) {

        myVH.textView.setText(list.get(i));

//        if (monItemClickListener != null) {
//            myVH.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = myVH.getLayoutPosition();
//                    monItemClickListener.onItemClick(myVH.itemView, pos);
//                }
//            });
//            myVH.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    int pos = myVH.getLayoutPosition();
//                    monItemClickListener.onItemLongClick(myVH.itemView, pos);
//                    return true;
//                }
//            });

//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
