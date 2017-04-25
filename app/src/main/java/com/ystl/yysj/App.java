package com.ystl.yysj;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.ystl.yysj.db.AppDataBaseHelper;
import com.ystl.yysj.ui.login.bean.UserInfo;
import com.ystl.yysj.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdi on 17/4/11.
 */
public class App extends Application {

    private static App mInstance;
    public static List<Activity> mActivityList = new ArrayList<>();
    private static UserInfo userInfo;

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppDataBaseHelper.getInstance(mInstance);

    }

    /**
     * 获取用户的信息
     */
    public UserInfo getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        } else {
            String s = SharedPreferencesUtil.getString(this, Constants.USER_ID);
            Log.e("登录用户名", s);
            userInfo = AppDataBaseHelper.getInstance(this).getUserInfo(s);
            if (userInfo != null) {
                Log.e("登录用户信息", userInfo.toString());
            } else {
                Log.e("登录用户信息为空", "--------------");
            }
        }
        return userInfo;
    }


    /**
     * 保存登录成功之后用户的信息
     *
     * @param result
     */
    public void setUserInfo(UserInfo result) {
        userInfo = result;
        if (userInfo != null) {
            SharedPreferencesUtil.putString(this, Constants.USER_ID, userInfo.uid + "");
            AppDataBaseHelper.getInstance(this).insertUser(userInfo);
        }
    }

    public void addActivity(Activity activity) {
        if (activity != null) {
            mActivityList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityList.remove(activity);
        }
    }
}
