package com.ystl.yysj.base;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.ystl.yysj.network.request.BaseRequest;
import com.ystl.yysj.network.utils.DialogUtils;


/**
 * 描    述：对于网络请求是否需要弹出进度对话框
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {
    private Activity activity;
    private boolean showDialog = true;

    public DialogCallback(Activity activity, boolean... showDialog) {
        super();
        this.activity = activity;
        if (showDialog != null && showDialog.length > 0) {
            this.showDialog = showDialog[0];
        }
    }


    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //网络请求前显示对话框
        if (showDialog) {
            DialogUtils.showDialogForLoading(activity, true);
        }
    }

    @Override
    public void onAfter(@Nullable T t, @Nullable Exception e) {
        super.onAfter(t, e);
        //网络请求结束后关闭对话框
        DialogUtils.hideDialogForLoading();
    }
}
