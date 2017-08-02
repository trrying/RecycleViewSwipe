package com.owm.clear.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * base fragment
 * Created by owm on 2017/8/1.
 */

public class BaseFragment extends Fragment {

    private final StaticHandler handler = new StaticHandler(this);

    public final Handler getHandler() {
        return handler;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private static class StaticHandler extends Handler {

        private WeakReference<BaseFragment> fragment;

        public StaticHandler(BaseFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (fragment.get() != null) {
                fragment.get().handleMessage(msg);
            }
        }
    }

    protected void handleMessage(Message msg) {

    }

    public boolean onBackPressed() {
        return false;
    }

}
