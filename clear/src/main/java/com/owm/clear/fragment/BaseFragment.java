package com.owm.clear.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

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
}
