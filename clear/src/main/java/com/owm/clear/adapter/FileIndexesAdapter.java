package com.owm.clear.adapter;

import com.owm.clear.R;
import com.owm.recyclerfastadapterlib.FastAdapter;
import com.owm.recyclerfastadapterlib.FastViewHolder;

import java.io.File;
import java.util.List;

/**
 * 文件索引
 * Created by owm on 2017/8/2.
 */

public class FileIndexesAdapter extends FastAdapter<File> {

    public FileIndexesAdapter(List<File> data) {
        super(R.layout.item_file_indexes, data);
    }

    @Override
    public void convert(FastViewHolder holder, File item) {
        holder.setText(R.id.tv_dir_name, item.getName() + ((holder.getAdapterPosition() == data.size() - 1) ? "" : " -> "));
    }
}
