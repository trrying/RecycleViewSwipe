package com.owm.clear.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.owm.clear.R;
import com.owm.clear.adapter.ClearFileAdapter;
import com.owm.recyclerfastadapterlib.FastAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.owm.clear.app.Constans.REFRESH_DIR;
import static com.owm.clear.app.Constans.REFRESH_LIST;
import static com.owm.clear.app.Constans.SCAN_START;
import static com.owm.clear.app.Constans.SCAN_STOP;

/**
 *
 * Created by owm on 2017/8/1.
 */

public class ClearFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, FastAdapter.OnItemChildClickListener {
    private static final int MATCH_FILE_SIZE = 1024 * 1024 * 10; //50m


    private SwipeRefreshLayout srl_clearFile;
    private RecyclerView rv_clearFile;

    private LinearLayout ll_scan;
    private TextView tv_scan_dir;

    private ClearFileAdapter mAdapter;
    private List<File> data = new ArrayList<>();

    private Thread scanBigFileThread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clear, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        scanBigFileThread = new Thread() {

        };
        scanBigFileThread.start();
    }



    private void initView(View view) {
        srl_clearFile = (SwipeRefreshLayout) view.findViewById(R.id.srl_clearFile);
        rv_clearFile = (RecyclerView) view.findViewById(R.id.rv_clearFile);

        ll_scan = (LinearLayout) view.findViewById(R.id.ll_scan);
        tv_scan_dir = (TextView) view.findViewById(R.id.tv_scan_dir);

        srl_clearFile.setOnRefreshListener(this);
    }

    private void refreshUI() {
        if (mAdapter == null) {
            mAdapter = new ClearFileAdapter(data);
            mAdapter.setOnItemChildClickListener(this);
            rv_clearFile.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_clearFile.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            rv_clearFile.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case REFRESH_LIST:
                refreshUI();
                break;
            case REFRESH_DIR:
                tv_scan_dir.setText(msg.obj.toString());
                break;
            case SCAN_START:
                ll_scan.setVisibility(View.VISIBLE);
                tv_scan_dir.setText("");
                break;
            case SCAN_STOP:
                ll_scan.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onRefresh() {
        rv_clearFile.postDelayed(new Runnable() {
            @Override
            public void run() {
                srl_clearFile.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onItemChildClick(FastAdapter<?> adapter, View itemView, View childView, int position) {
        if (childView.getId() == R.id.tv_cut) {

        }
    }
}
