package com.owm.clear.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.owm.clear.R;
import com.owm.clear.adapter.ViewPagerAdapter;
import com.owm.clear.fragment.ClearFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 0x1;

    private ViewPager vp_content;
    private ViewPagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkReadPermission();
        initView();
        init();
    }

    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_READ_STORAGE) {

        }
    }

    private void initView() {
        vp_content = (ViewPager) findViewById(R.id.vp_content);
    }

    private void init() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ClearFragment());
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_content.setAdapter(pagerAdapter);
    }

}
