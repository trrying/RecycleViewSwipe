package com.owm.clear.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.owm.clear.fragment.BaseFragment;

import java.util.List;

/**
 *
 * Created by owm on 2017/8/2.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null && !fragments.isEmpty()) {
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof BaseFragment) {
                        if(((BaseFragment) fragment).onBackPressed()) {
                            return true;
                        }
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
