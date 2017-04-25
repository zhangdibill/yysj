package com.ystl.yysj.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by liuyandong on 2015/10/10.
 */
public class ToastUtil {
    private static Toast mToast;

    /**
     * 显示toast，解决toast叠加问题
     *
     * @param text toast显示的文本
     */
    public static void showToast(Context context, CharSequence text, int duration) {
        if (TextUtils.isEmpty(text.toString().trim())) return;
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }


    public static void showToast(Context context, CharSequence text) {
        if (mToast == null) {
            if (context != null) {
                mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            }
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * resId形式文本
     *
     * @param context  文本
     * @param resId    字符串id
     * @param duration 显示时长
     */
    public static void showToast(Context context, int resId, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, duration);
        } else {
            mToast.setText(resId);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    /**
     * 取消toast
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
