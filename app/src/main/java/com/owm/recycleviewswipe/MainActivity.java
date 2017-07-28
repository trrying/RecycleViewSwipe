package com.owm.recycleviewswipe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.owm.recyclerfastadapterlib.FastAdapter;
import com.owm.recyclerviewswipelib.RecyclerViewSwipeTouch;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements FastAdapter.OnItemClickListener, FastAdapter.OnItemLongClickListener, FastAdapter.OnItemChildClickListener {

    private RecyclerViewSwipeTouch swipeTouch;
    private RecyclerView rv_content;
    private NumAdapter adapter;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                refreshUI();
            }
        });
        init();
        rv_content = (RecyclerView) findViewById(R.id.rv_content);
        swipeTouch = new RecyclerViewSwipeTouch(rv_content);
        swipeTouch.setSwipeCallBack(new RecyclerViewSwipeTouch.SwipeCallBack() {
            @Override
            public void onRemoveView(View removeView, int position) {
                data.remove(position);
                refreshUI();
            }
        });
    }

    private void init() {
        data.clear();
        for (int i = 0; i < 50; i++) {
            data.add("swipe " + i);
        }
    }

    private void refreshUI() {
        if (adapter == null) {
            adapter = new NumAdapter(data);
            adapter.setOnItemClickListener(this);
            adapter.setOnItemLongClickListener(this);
            adapter.setOnItemChildClickListener(this);
            rv_content.setLayoutManager(new LinearLayoutManager(this));
            rv_content.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            rv_content.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(FastAdapter<?> adapter, View itemView, int position) {
        Toast.makeText(this, "position:"+position, 0).show();
    }

    @Override
    public boolean onItemLongClick(FastAdapter<?> adapter, View itemView, int position) {
        Toast.makeText(this, "long position:"+position, 0).show();
        return true;
    }

    @Override
    public void onItemChildClick(FastAdapter<?> adapter, View itemView, View childView, int position) {
        Toast.makeText(this, "child position:"+position, 0).show();
    }
}
