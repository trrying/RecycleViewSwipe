package com.owm.clear.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.owm.clear.R;
import com.owm.clear.adapter.FileIndexesAdapter;
import com.owm.clear.adapter.FileManagerAdapter;
import com.owm.clear.util.FileUtils;
import com.owm.recyclerfastadapterlib.FastAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件浏览
 * Created by owm on 2017/8/2.
 */

public class FileManagerFragment extends BaseFragment implements FastAdapter.OnItemClickListener {

    private RecyclerView rv_file_indexes;
    private FileIndexesAdapter fileIndexesAdapter;
    private List<File> fileIndexesList = new ArrayList<>();

    private RecyclerView rv_file_manager;
    private FileManagerAdapter fileManagerAdapter;
    private List<File> fileManagerList = new ArrayList<>();

    private List<File> rootFileList = new ArrayList<>();

    private File currentDir;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_manager, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initView(View view) {
        rv_file_indexes = (RecyclerView) view.findViewById(R.id.rv_file_indexes);
        rv_file_manager = (RecyclerView) view.findViewById(R.id.rv_file_manager);
    }

    private void initData() {
        initRootFileList();
    }

    private void initRootFileList() {
        rootFileList.clear();

        String innerSDCardPath = FileUtils.getInnerSDCardPath();
        String[] extSDCardPath = FileUtils.getExtSDCardPath(getContext());
        if (extSDCardPath != null) {
            if (!Arrays.asList(extSDCardPath).contains(innerSDCardPath)) {
                addRootFile(innerSDCardPath);
            }
            for (String anExtSDCardPath : extSDCardPath) {
                addRootFile(anExtSDCardPath);
            }
        }
        fileManagerList.clear();
        fileManagerList.addAll(rootFileList);

        refreshFIleManager();
        fileIndexesList.clear();
        refreshFileIndexes();
    }

    private void addRootFile(String innerSDCardPath) {
        try{
            File file = new File(innerSDCardPath);
            if (file.isDirectory() && file.exists()) {
                rootFileList.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshFileIndexes() {
        if (fileIndexesAdapter == null) {
            fileIndexesAdapter = new FileIndexesAdapter(fileIndexesList);
            fileIndexesAdapter.setOnItemClickListener(this);
            rv_file_indexes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//            rv_file_indexes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
            rv_file_indexes.setAdapter(fileIndexesAdapter);
        } else {
            fileIndexesAdapter.notifyDataSetChanged();
        }
    }

    private void refreshFIleManager() {
        if (fileManagerAdapter == null) {
            fileManagerAdapter = new FileManagerAdapter(fileManagerList);
            fileManagerAdapter.setOnItemClickListener(this);
            rv_file_manager.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            rv_file_manager.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_file_manager.setAdapter(fileManagerAdapter);
        } else {
            fileManagerAdapter.notifyDataSetChanged();
        }
    }

    private void setCurrentDir(File file) {

        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        fileManagerList.clear();
        fileManagerList.addAll(Arrays.asList(files));
        refreshFIleManager();
        int size = fileIndexesList.size();
        if (size == 0 || !FileUtils.equals(fileIndexesList.get(size - 1), file)) {
            fileIndexesList.add(file);
            refreshFileIndexes();
        }
        currentDir = file;
    }

    @Override
    public void onItemClick(FastAdapter<?> adapter, View itemView, int position) {
        if (adapter.equals(fileIndexesAdapter)) {
            backHistory(position);
        } else if (adapter.equals(fileManagerAdapter)) {
            File file = fileManagerList.get(position);
            if (file.isDirectory() && file.exists()) {
                setCurrentDir(file);
            }
        }
    }

    private boolean backHistory(int position) {
        boolean result = false;
        File file = fileIndexesList.get(position);
        if (!FileUtils.equals(file, currentDir) && file.isDirectory() && file.exists()) {
            if (position < fileIndexesList.size()) {
                for (int i = position + 1; i < fileIndexesList.size(); i++) {
                    fileIndexesList.remove(i);
                }
                refreshFileIndexes();
            }
            setCurrentDir(file);
            result = true;
        }
        return result;
    }

    private boolean backHistory() {
        boolean result;
        if (fileIndexesList.size() < 1) {
            result = false;
        } else if (fileIndexesList.size() == 1) {
            initRootFileList();
            result = true;
        } else {
            result = backHistory(fileIndexesList.size() - 2);
        }
        return result;
    }

    @Override
    public boolean onBackPressed() {
        return backHistory() || super.onBackPressed();
    }
}
