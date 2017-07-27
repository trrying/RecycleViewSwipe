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

public class FastViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;

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

}
