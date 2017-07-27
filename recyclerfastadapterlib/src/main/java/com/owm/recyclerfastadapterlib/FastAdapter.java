package com.owm.recyclerfastadapterlib;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * recyclerView fast create adapter
 * Created by owm on 2017/7/27.
 */

public abstract class FastAdapter<T> extends RecyclerView.Adapter<FastViewHolder> {

    protected int layoutResId;
    protected List<T> data;

    public FastAdapter(@LayoutRes int layoutResId, List<T> data) {
        this.layoutResId = layoutResId;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public FastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, null);
        return new FastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FastViewHolder holder, int position) {
        convert(holder, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract void convert(FastViewHolder holder, T item);

}
