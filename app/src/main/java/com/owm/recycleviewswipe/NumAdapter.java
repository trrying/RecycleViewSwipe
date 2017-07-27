package com.owm.recycleviewswipe;

import com.owm.recyclerfastadapterlib.FastAdapter;
import com.owm.recyclerfastadapterlib.FastViewHolder;

import java.util.List;

/**
 *
 * Created by owm on 2017/7/27.
 */

public class NumAdapter extends FastAdapter<String> {

    public NumAdapter(List<String> data) {
        super(R.layout.item_number, data);
    }

    @Override
    public void convert(FastViewHolder holder, String item) {
        holder.setText(R.id.tv_number, item);
    }

}
