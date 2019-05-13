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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyVH>{
    /**--------------------------------------------------------------------*/
    /** itemClickLitener*/

    public interface onItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    private onItemClickListener monItemClickListener;
    public void setOnItemClickListener(onItemClickListener monItemClickListener){
        this.monItemClickListener = monItemClickListener;
    }



    private Context mContext;
    private ArrayList<DataRecyclerView> dataRecyclerViewArrayList;



    public MyRecyclerViewAdapter(Context context, ArrayList<DataRecyclerView> arrayList){

        mContext = context;
        dataRecyclerViewArrayList = arrayList;
    }

/**--------------------------------------------------------------------*/
                     /** ViewHolder*/
    public class MyVH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_imageview)
        ImageView item_imageview;
        @BindView(R.id.item_textview)
        TextView item_textview;
        public MyVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview,viewGroup,false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH myVH, int i) {
    Picasso.with(mContext).load(dataRecyclerViewArrayList.get(i).getImage_url()).resize(120,60).into(myVH.item_imageview);
    myVH.item_textview.setText(dataRecyclerViewArrayList.get(i).getImage_name());

    if (monItemClickListener!=null){
        myVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = myVH.getLayoutPosition();
                monItemClickListener.onItemClick(myVH.itemView,pos);
            }
        });
        myVH.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = myVH.getLayoutPosition();
                monItemClickListener.onItemLongClick(myVH.itemView,pos);
                return true;
            }
        });

    }

    }

    @Override
    public int getItemCount() {
        return dataRecyclerViewArrayList.size();
    }


}
