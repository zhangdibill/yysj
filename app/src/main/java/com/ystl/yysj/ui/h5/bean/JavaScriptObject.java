package com.ystl.yysj.ui.h5.bean;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * 、
 * js调用native的方法使用的类
 * 调用javascript方法的类
 * 在js里面定义的方法都需要在这个类里面进行定义
 */
public class JavaScriptObject {
    Context mContext;

    public JavaScriptObject(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * H5里边的统计事件
     * @param key  点击事件的名称
     */
    @JavascriptInterface
    public void jsCallWebViewUmeng(String key) {
        Log.i("BBBB", "jsCallWebViewUmeng   key = " + key);
//        MobclickAgent.onEvent(mContext, key);
    }

}