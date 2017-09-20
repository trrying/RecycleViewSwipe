package com.owm.findimg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

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
                testFindImage();
            }
        });
    }

    public void testFindImage() {
        String dirPath = Environment.getExternalStorageDirectory() + "/apk/find/";
        String mubiao = dirPath + "guai_1.jpg";
        String yuantu = dirPath + "map_9_34_33.jpg";
        try {
            Boolean isBelong = FindImgUtils.FindImg(mubiao, yuantu);
            System.out.println("isBelong --> " + isBelong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testFindImage1() {
        String dirPath = Environment.getExternalStorageDirectory() + "/apk/find/";
        String mubiao = dirPath + "kuan_1.jpg";
        String yuantu = dirPath + "map_9_34_33.jpg";
        try {
            Boolean isBelong = FindImgUtils.FindImg(mubiao, yuantu);
            System.out.println("isBelong --> " + isBelong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void swipe() {
        RootCmd.execRootCmd("sleep 0.1 && input keyevent " + KeyEvent.KEYCODE_HOME);
        for (int i = 0; i < 3; i++) {
            RootCmd.execRootCmd("sleep 0.1 && input swipe 100 100 300 100");
        }
        for (int i = 0; i < 3; i++) {
            RootCmd.execRootCmd("sleep 0.1 && input swipe 300 100 100 100");
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
//            testFindImage1();
            startCheckAccissibility();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void startCheckAccissibility(){
        Intent intent = new Intent(this, AccessibilityMonitorService.class);
        startService(intent);
    }


}
