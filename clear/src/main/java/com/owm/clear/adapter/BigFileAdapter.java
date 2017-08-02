package com.owm.clear.adapter;

import android.view.View;

import com.owm.clear.R;
import com.owm.clear.util.FileUtils;
import com.owm.recyclerfastadapterlib.FastAdapter;
import com.owm.recyclerfastadapterlib.FastViewHolder;

import java.io.File;
import java.util.List;

/**
 *
 * Created by owm on 2017/8/1.
 */

public class BigFileAdapter extends FastAdapter<File>{

    public BigFileAdapter(List<File> data) {
        super(R.layout.item_file_info, data);
    }

    @Override
    public void convert(FastViewHolder holder, File item) {
        holder.setText(R.id.tv_file_name, item.getName())
            .setText(R.id.tv_file_path, item.getParent());
        if (item.isFile()) {
            holder.getView(R.id.tv_file_size).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_file_size, "size: "+ FileUtils.getSize(item));
        } else {
            holder.getView(R.id.tv_file_size).setVisibility(View.GONE);
        }
    }

}
