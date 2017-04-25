package com.ystl.yysj.ui.h5.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ystl.yysj.R;
import com.ystl.yysj.base.BaseActivity;
import com.ystl.yysj.network.utils.DialogUtils;
import com.ystl.yysj.ui.h5.MWebChromeClient;
import com.ystl.yysj.ui.h5.bean.JavaScriptObject;
import com.ystl.yysj.ui.h5.bean.WebBean;
import com.ystl.yysj.ui.h5.view.BaseWebView;
import com.ystl.yysj.utils.TurntoActivityUtil;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;


/**
 * 普通的 webview  只需要传WebBean过来 通过调用
 */
public class NormalH5Activity extends BaseActivity {

    WebBean webBean;
    @BindView(R.id.wv_webView)
    public BaseWebView mBaseWebView;

    @BindView(R.id.progressBar_webview)
    public ProgressBar progressBar_webview;
    public JavaScriptObject javaScriptObject;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra(TurntoActivityUtil.BEAN);
        if (serializable != null && serializable instanceof WebBean) {
            webBean = (WebBean) serializable;
        }
        if (webBean == null) {
            webBean = new WebBean();
        }
        initView();
        loadData();
    }

    public void initView() {
        if (mBaseWebView != null) {
            WebSettings setting = mBaseWebView.getSettings();
            setting.setDefaultTextEncodingName("utf-8");
//            setting.setJavaScriptEnabled(true);
            mBaseWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
            setting.setJavaScriptEnabled(true);//支持js
            mBaseWebView.setWebViewClient(new MyWebViewClient());
            mBaseWebView.setWebChromeClient(new MWebChromeClient(this) {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    Log.i("BBBB", "title = " + title);
                    if (TextUtils.isEmpty(webBean.url)) {
                        ((NormalH5Activity) mContext).setTitle(title);
                    } else {
                        setTitle(webBean.title, R.drawable.title_niv_back, 0);
                    }
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressBar_webview.setProgress(newProgress);
                    if (newProgress >= 95) {
                        progressBar_webview.setVisibility(View.GONE);
                    }
                }
            });
            javaScriptObject = new JavaScriptObject(mContext);
            mBaseWebView.addJavascriptInterface(javaScriptObject, "WebViewFunc");
            //屏蔽长按
            mBaseWebView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }


    public void loadData() {
        if (TextUtils.isEmpty(webBean.url)) {
            return;
        }
        new AsyncTask<String, Void, Integer>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                DialogUtils.showDialogForLoading(NormalH5Activity.this, true);
            }

            @Override
            protected Integer doInBackground(String... params) {
                int responseCode = -1;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    responseCode = connection.getResponseCode();
                } catch (Exception e) {
                }
                return responseCode;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result != 200) {
                    mBaseWebView.setVisibility(View.GONE);
                    progressBar_webview.setVisibility(View.GONE);
                    showErrorDialog();
                } else {
                    mBaseWebView.setVisibility(View.VISIBLE);
                    progressBar_webview.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(webBean.url)) {
                        mBaseWebView.loadUrl("file:///android_asset/pager.html");
                    } else {
                        mBaseWebView.loadUrl(webBean.url);
                    }
                }
            }
        }.execute(webBean.url);
    }

    public class MyWebViewClient extends WebViewClient {
        boolean isFail = false;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mBaseWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            DialogUtils.showDialogForLoading(NormalH5Activity.this, false);
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onPageFinished(final WebView view, String url) {
            DialogUtils.hideDialogForLoading();
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(final WebView view, int errorCode, String description, final String failingUrl) {
            DialogUtils.hideDialogForLoading();
            showErrorDialog();
        }
    }

    public void showErrorDialog() {
//        com.zhtx.salesman.utils.DialogUtils.getNormalDialog(NormalH5Activity.this, "加载出错", "", "结束", "重新加载", new com.zhtx.salesman.utils.DialogUtils.OnDialogClickListener() {
//            @Override
//            public void onDialogClick(boolean isLeft) {
//                if (!isLeft) {
//                    loadData();
//                } else {
//                    finish();
//                }
//            }
//        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mBaseWebView.canGoBack()) {
            mBaseWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClickEvent(View view) {

    }
}
