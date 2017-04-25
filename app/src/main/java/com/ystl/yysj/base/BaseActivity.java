package com.ystl.yysj.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ystl.yysj.App;
import com.ystl.yysj.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public Toolbar toolBar;
    public View errorView;
    public View notDataView;
    // 全局的Context
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();

        mContext = this;
        App.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    /**
     * 免转换类型findView
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            mTintManager.setTintColor(this.getResources().getColor(R.color.system_bar));
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 子类可以重写改变状态栏颜色
     */
    protected int setStatusBarColor() {
        return getColorPrimary();
    }

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    protected boolean translucentStatusBar() {
        return false;
    }

    /**
     * 获取主题颜色
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取深主题颜色
     */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void finishActivity(View view) {
        if ((view instanceof RelativeLayout) && view.getId() == R.id.finish) {
            finish();
        }
    }

    public Toolbar setTitle(String title) {
        return this.setTitle(title, 0, 0);
    }

    public Toolbar setTitle(String title, int leftImageRex, int rightImagRec) {
        return this.setTitle(title, "", leftImageRex, rightImagRec);
    }

    public Toolbar setTitle(String title, String rightStr, int leftImageRex, int rightImagRec) {
        return this.setTitle(title, rightStr, leftImageRex, rightImagRec, 0);
    }

    /***
     * 设置标题
     *
     * @param title        标题
     * @param rightStr     右边文字
     * @param leftImageRex 左边图标
     * @param rightImagRec 右边图标
     * @return
     */
    public Toolbar setTitle(String title, String rightStr, int leftImageRex, int rightImagRec, int rightTextColor) {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        if (toolBar != null) {
            toolBar.setTitle("");
            setSupportActionBar(toolBar);
            View titleView = toolBar.findViewById(R.id.tv_title);
            if (titleView instanceof TextView) {
                TextView titleTextView = (TextView) titleView;
                titleTextView.setText(title);
            }

            ImageView img_left = (ImageView) toolBar.findViewById(R.id.img_left);
            if (leftImageRex != 0) {
                img_left.setImageResource(leftImageRex);
                img_left.setVisibility(View.VISIBLE);
            } else {
                img_left.setVisibility(View.GONE);
            }

            ImageView imageView = (ImageView) toolBar.findViewById(R.id.img_right);
            if (rightImagRec != 0) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(this);
                imageView.setImageResource(rightImagRec);
            } else {
                imageView.setVisibility(View.GONE);
            }

            TextView rightTextView = (TextView) toolBar.findViewById(R.id.tv_rightText);
            if (!TextUtils.isEmpty(rightStr)) {
                rightTextView.setVisibility(View.VISIBLE);
                rightTextView.setOnClickListener(this);
                rightTextView.setText(rightStr);
                if (rightTextColor != 0) {
                    rightTextView.setTextColor(rightTextColor);
                }
            } else {
                rightTextView.setVisibility(View.GONE);
            }


            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setId(R.id.finish);
                    onClickEvent(v);
                }
            });
        }
        return toolBar;
    }

    /**
     * 显示返回键
     *
     * @param visible
     */
    public void setLeftVisible(int visible) {
        if (toolBar != null) {
            ImageView img_left = (ImageView) toolBar.findViewById(R.id.img_left);
            img_left.setVisibility(visible);
        }
    }

    public String getTittle() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        String tittle = "";
        if (tv_title != null) {
            tittle = tv_title.getText().toString().trim();
        }
        return tittle;
    }

    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }

    /**
     * tool bar 的点击事件
     *
     * @param view
     */
    abstract public void onClickEvent(View view);

    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().removeActivity(this);
    }

    /**
     * 获得当前程序版本 versionName
     *
     * @return String
     * @throws Exception
     */
    protected String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }

    /**
     * 获得全局的Context
     *
     * @return Context
     */

    public Context getContext() {
        return mContext;
    }

    public void setEmptyAndErrorView(View rootView) {

        ViewGroup viewGroup = null;
        if (rootView != null) {
            viewGroup = (ViewGroup) rootView.getParent();
        }
        notDataView = getLayoutInflater().inflate(R.layout.empty_view, viewGroup, false);
        errorView = getLayoutInflater().inflate(R.layout.error_view, viewGroup, false);
    }
}
