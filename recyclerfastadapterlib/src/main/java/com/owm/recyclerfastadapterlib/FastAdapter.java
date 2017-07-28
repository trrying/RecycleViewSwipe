package com.owm.recyclerfastadapterlib;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * recyclerView fast create adapter
 * Created by owm on 2017/7/27.
 */

public abstract class FastAdapter<T> extends RecyclerView.Adapter<FastViewHolder> {

    protected int layoutResId;
    protected List<T> data;

    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;
    protected OnItemChildClickListener onItemChildClickListener;
    protected OnItemChildLongClickListener onItemChildLongClickListener;

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
        final FastViewHolder viewHolder = new FastViewHolder(view);

        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(FastAdapter.this, viewHolder.itemView, viewHolder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLongClick(FastAdapter.this, viewHolder.itemView, viewHolder.getAdapterPosition());
                }
            });
        }
        viewHolder.setAdapter(this);
        return viewHolder;
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        if (this.data != data) {
            this.data.clear();
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnItemChildClickListener getOnItemChildClickListener() {
        return onItemChildClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return onItemChildLongClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.onItemChildLongClickListener = onItemChildLongClickListener;
    }

    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView has been clicked.
         * @param adapter the adapter
         * @param itemView The item view within the RecyclerView that was click
         * @param position The position of the view in the adapter
         */
        void onItemClick(FastAdapter<?> adapter, View itemView, int position);
    }

    public interface OnItemLongClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView has been long clicked.
         * @param adapter The adapter
         * @param itemView The item view within the RecyclerView that was long click
         * @param position The position of the view in the adapter
         * @return true if The callback consumed the long click, false otherwise
         */
        boolean onItemLongClick(FastAdapter<?> adapter, View itemView, int position);
    }

    public interface OnItemChildClickListener {

        /**
         * Callback method to be invoked when a view of item in this RecyclerView has been clicked.
         * @param adapter The adapter
         * @param itemView The item view within the RecyclerView that was click
         * @param childView The view of itemView within the RecyclerView that was click.
         * @param position The position of the view in the adapter
         */
        void onItemChildClick(FastAdapter<?> adapter, View itemView, View childView, int position);
    }

    public interface OnItemChildLongClickListener {

        /**
         * Callback method to be invoked when a view of item in this RecyclerView has been Long clicked.
         * @param adapter The adapter
         * @param itemView The item view within the RecyclerView that was Long click
         * @param childView The view of itemView within the RecyclerView that was Long click.
         * @param position The position of the view in the adapter
         * @return true if The callback consumed the long click, false otherwise
         */
        boolean onItemChildLongClick(FastAdapter<?> adapter, View itemView, View childView, int position);
    }

}
