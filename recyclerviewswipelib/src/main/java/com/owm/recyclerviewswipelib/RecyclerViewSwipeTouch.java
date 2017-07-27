package com.owm.recyclerviewswipelib;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Locale;

/**
 * RecyclerView滑动移除
 * Created by owm on 2017/7/26.
 */

public class RecyclerViewSwipeTouch implements View.OnTouchListener {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private RecyclerView mRecyclerView;

    private int mOrientation;

    private float downX;
    private float downY;

    private View touchView;

    private VelocityTracker mVelocityTracker;

    private long mAnimationTime;

    private boolean mPause;

    private boolean mSwipe;

    /** 当前需要判断手势的垂直方向 **/
    private boolean mVerticalDirection;

    private SwipeCallBack mSwipeCallBack;

    private float deltaX;
    private float deltaY;

    public RecyclerViewSwipeTouch(RecyclerView recyclerView) {
        this(recyclerView, VERTICAL);
    }

    public RecyclerViewSwipeTouch(RecyclerView recyclerView, int orientation) {
        this.mRecyclerView = recyclerView;
        this.mOrientation = orientation;
        mRecyclerView.setOnTouchListener(this);
        mAnimationTime = mRecyclerView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (mPause) {
                    break;
                }
                downX = event.getX();
                downY = event.getY();
                touchView = mRecyclerView.findChildViewUnder(downX, downY);
                if (touchView != null) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mVelocityTracker == null || touchView == null) {
                    break;
                }
                mVelocityTracker.addMovement(event);

                deltaX = event.getX() - downX;
                deltaY = event.getY() - downY;
                if (mOrientation == VERTICAL) {
                    if (!mVerticalDirection && Math.abs(deltaX) > 30) {
                        touchView.setTranslationX(deltaX);
                        return mSwipe = true;
                    }
                    //先在竖直方向滑动之后就不再消费事件了
                    if (Math.abs(deltaY) > 30) {
                        mVerticalDirection = true;
                    }
                } else {

                }
                if (mSwipe) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mVelocityTracker == null || touchView == null) {
                    reset();
                    break;
                }
                //计算1000毫秒划过多少个像素
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                float yVelocity = mVelocityTracker.getYVelocity();
                System.out.println(String.format(Locale.getDefault(), "xVelocity:%s yVelocity:%s", xVelocity, yVelocity));

                deltaX = event.getX() - downX;
                deltaY = event.getY() - downY;
                int width = mRecyclerView.getWidth();
                if (mOrientation == VERTICAL) {
                    if (mSwipe) {
                        //滑动大过宽度一半，或者速率大过200
                        if (Math.abs(deltaX) > width / 2 || Math.abs(xVelocity) > 1000) {
                            final int position = mRecyclerView.getChildAdapterPosition(touchView);
                            touchView.animate().setDuration(mAnimationTime).translationX(deltaX > 0 ? width : -width).setListener(new BaseAnimationListener(){
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (mSwipeCallBack != null) {
                                        mSwipeCallBack.onRemoveView(touchView, position);
                                    }
                                    touchView.animate().setListener(null);
                                    resetViewLocation(0);
                                }
                            });
                        } else {
                            resetViewLocation();
                        }
                    }
                } else {

                }
                reset();
                break;
            case MotionEvent.ACTION_CANCEL:
                reset();
                resetViewLocation();
                break;
        }

        return false;
    }

    public void reset() {
        mSwipe = false;
        mVerticalDirection = false;
    }

    public void resetViewLocation() {
        resetViewLocation(mAnimationTime);
    }

    public void resetViewLocation(long duration) {
        if (touchView != null) {
            touchView.animate().translationX(0).setDuration(duration);
        }
    }

    public SwipeCallBack getSwipeCallBack() {
        return mSwipeCallBack;
    }

    public void setSwipeCallBack(SwipeCallBack swipeCallBack) {
        this.mSwipeCallBack = swipeCallBack;
    }

    public boolean isEnable() {
        return mPause;
    }

    public void setEnable(boolean enable) {
        this.mPause = enable;
    }

    public interface SwipeCallBack {
        void onRemoveView(View removeView, int position);
    }

}
