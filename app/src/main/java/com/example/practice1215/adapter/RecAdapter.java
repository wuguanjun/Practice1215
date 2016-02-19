package com.example.practice1215.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by guanjun on 2016/1/25.
 */
public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyHolder> {
    List l;
    Context context;

    public RecAdapter(Context context, List l) {
        this.context = context;
        this.l = l;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder myHolder = null;//= new MyHolder(LayoutInflater.from(context).inflate(null));
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
