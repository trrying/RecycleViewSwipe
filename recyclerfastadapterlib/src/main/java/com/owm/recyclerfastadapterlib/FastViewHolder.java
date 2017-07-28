package com.owm.recyclerfastadapterlib;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 *
 * Created by owm on 2017/7/27.
 */

public class FastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private SparseArray<View> views;
    private FastAdapter<?> adapter;

    protected FastViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public FastViewHolder setText(@IdRes int viewId, CharSequence text) {
        ((TextView) getView(viewId)).setText(text);
        return this;
    }

    public FastViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        ((TextView) getView(viewId)).setText(resId);
        return this;
    }

    public void addOnClickListener(@IdRes int viewId) {
        getView(viewId).setOnClickListener(this);
    }

    public void removeOnClickListener(@IdRes int viewId) {
        getView(viewId).setOnClickListener(null);
    }

    public void addOnLongClickListener(@IdRes int viewId) {
        getView(viewId).setOnLongClickListener(this);
    }

    public void removeOnLongClickListener(@IdRes int viewId) {
        getView(viewId).setOnLongClickListener(null);
    }

    public FastAdapter<?> getAdapter() {
        return adapter;
    }

    public void setAdapter(FastAdapter<?> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        if (adapter != null && adapter.getOnItemChildClickListener() != null) {
            adapter.getOnItemChildClickListener().onItemChildClick(adapter, itemView, v, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return adapter != null && adapter.getOnItemChildLongClickListener() != null && adapter.getOnItemChildLongClickListener().onItemChildLongClick(adapter, itemView, v, getAdapterPosition());
    }
}
