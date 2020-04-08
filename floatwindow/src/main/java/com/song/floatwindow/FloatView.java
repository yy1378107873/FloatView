package com.song.floatwindow;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

/**
 * @author : songjun
 * @create : 2020/3/20
 */
public class FloatView extends FrameLayout implements View.OnTouchListener {
    private FloatViewCallBack callBack;
    /**
     * 拖动最小有效偏移量
     */
    public static final int MIN_OFFSET = 5;
    private View contentView;
    /**
     * 触摸点按下相对view的左上角的位置
     * 用于跟滑动后手指相对屏幕左上角位置计算view相对屏幕的位置x、y
     */
    private int downX;
    private int downY;

    /**
     * 触摸滑动后手指相对屏幕左上角的位置坐标
     */
    private float rawX;
    private float rawY;

    /**
     * 是否自动贴边
     */
    private boolean autoAlign = true;

    public FloatView(@NonNull Context context, View contentView,FloatViewCallBack callBack) {
        super(context);
        if (contentView.getParent() != null && contentView.getParent() instanceof ViewGroup) {
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
        addView(contentView);
        this.setOnTouchListener(this);
        this.callBack = callBack;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //如果有效自己处理触摸事件
                isIntercept = Math.abs(ev.getX() - downX) > MIN_OFFSET && Math.abs(ev.getY() - downY) > MIN_OFFSET;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
            case MotionEvent.ACTION_OUTSIDE://手指点击窗口外响应该事件:
                actionOutSide(event);
                break;
        }
        return false;
    }

    private void actionMove(MotionEvent event) {
        //拖动事件下一直计算坐标 然后更新悬浮窗位置
        //获取触摸点相对于屏幕左上角的坐标
        rawX = event.getRawX();
        rawY = event.getRawY()-getStatusBarHeight();
        if (callBack!=null){
            callBack.updateLocation(rawX - downX,rawY - downY);
        }
    }

    private void actionUp(MotionEvent event) {
        if (autoAlign&&callBack!=null){
            callBack.autoAlign(rawX);
        }
    }

    private void actionOutSide(MotionEvent event) {

    }

    /**
     * 用于获取系统状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        int identifier = Resources.getSystem().getIdentifier("status_bar_height",
                "dimen", "android");
        if (identifier > 0) {
            return Resources.getSystem().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public interface FloatViewCallBack{
        void updateLocation(float x, float y);
        void autoAlign(float rawX);
    }
}
