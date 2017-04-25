package com.ystl.yysj.network.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ystl.yysj.R;

/**
 * Created by LXH on 16/11/3.
 */

public  class DialogUtils {
    private static Dialog mLoadingDialog;
    static AnimationDrawable animationDrawable;
    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param cancelable 对话框是否可以取消
     */
    public static void showDialogForLoading(Activity context, boolean cancelable) {
        try {
            if (dialogIsShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
            ImageView iv_prgloading = (ImageView) view.findViewById(R.id.iv_prgloading);
            animationDrawable = (AnimationDrawable) iv_prgloading.getBackground();
            animationDrawable.start();

            mLoadingDialog = new Dialog(context, R.style.selectorDialog);
            mLoadingDialog.setCancelable(cancelable);
            mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean dialogIsShowing() {
        boolean b = false;
        if (mLoadingDialog != null) {
            b = mLoadingDialog.isShowing();
        }
        return b;
    }

    /**
     * 关闭加载对话框
     */
    public static void hideDialogForLoading() {
        try {
            if (animationDrawable != null && animationDrawable.isRunning()) {
                animationDrawable.stop();
                animationDrawable = null;
            }

            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
