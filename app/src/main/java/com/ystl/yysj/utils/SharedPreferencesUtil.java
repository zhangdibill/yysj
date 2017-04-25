package com.ystl.yysj.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ystl.yysj.App;

/**
 * Created by zhangdi on 2015/7/22.
 * <p/>
 * sp的相关操作
 */
public class SharedPreferencesUtil {

    public static boolean getBoolean(Context context, String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String prefName, String prefKey,
                                     boolean defaultValue) {
        return getSharedPreferences(context, prefName).getBoolean(prefKey, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean b) {
        getSharedPreferences(context).edit().putBoolean(key, b).commit();
    }

    public static void putBoolean(Context context, String prefName, String prefKey, boolean value) {
        getSharedPreferences(context, prefName).edit().putBoolean(prefKey, value).commit();
    }

    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    private static String getString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static String getString(Context context, String prefName, String prefKey,
                                   String defaultValue) {
        return getSharedPreferences(context, prefName).getString(prefKey, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static void putString(Context context, String prefName, String prefKey, String value) {
        getSharedPreferences(context, prefName).edit().putString(prefKey, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getSharedPreferences(context).getLong(key, -1L);
    }

    public static long getLong(Context context, String key, long defalutValue) {
        return getSharedPreferences(context).getLong(key, defalutValue);
    }

    public static long getLong(Context context, String prefName, String prefKey, long defaultValue) {
        return getSharedPreferences(context, prefName).getLong(prefKey, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        getSharedPreferences(context).edit().putLong(key, value).commit();
    }

    public static void putLong(Context context, String prefName, String prefKey, long value) {
        getSharedPreferences(context, prefName).edit().putLong(prefKey, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getSharedPreferences(context).getInt(key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static int getInt(Context context, String prefName, String prefKey, int defaultValue) {
        return getSharedPreferences(context, prefName).getInt(prefKey, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static void putInt(Context context, String prefName, String prefKey, int value) {
        getSharedPreferences(context, prefName).edit().putInt(prefKey, value).commit();
    }

    public static float getFloat(Context context, String prefName, String prefKey,
                                 float defaultValue) {
        return getSharedPreferences(context, prefName).getFloat(prefKey, defaultValue);
    }

    public static void putFloat(Context context, String prefName, String prefKey, float value) {
        getSharedPreferences(context, prefName).edit().putFloat(prefKey, value).commit();
    }

    public static void putFloat(Context context, String prefKey, float value) {
        getSharedPreferences(context).edit().putFloat(prefKey, value).commit();
    }


    /**
     * 移除SharedPreferences
     *
     * @param context
     * @param prefKey
     */
    public static void remove(Context context, String prefKey) {
        getSharedPreferences(context).edit().remove(prefKey).commit();
    }

    /**
     * 清除文件内所有内容
     * @param context
     * @param preName
     */
    public static void clear(Context context, String preName) {
        getSharedPreferences(context, preName).edit().clear().commit();
    }

    /**
     * 得到默认SharePreference
     *
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            context = App.getInstance().getApplicationContext();
        }
        return getSharedPreferences(context, null);
    }

    /**
     * 根据名字得到SharePreference
     *
     * @param context
     * @param prefName 独立存储文件
     * @return
     */
    public static SharedPreferences getSharedPreferences(@NonNull Context context, String prefName) {
        if (TextUtils.isEmpty(prefName)) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        }
    }

//    /**
//     * @param context
//     * @return
//     */
//    public static String getToken(Context context, String string) {
//        StringBuilder sBuilder = new StringBuilder();
//        sBuilder.append("userId=");
//        sBuilder.append(SharedPreferencesUtil.getInt(context, Constants.UID, -1));
//        sBuilder.append("&sign=");
//        sBuilder.append(SharedPreferencesUtil.getString(context, Constants.TOKEN));
//
//        String sign = Utils.getMD5(string + sBuilder.toString());
//        String value = String.format("userId=%d&sign=%s",
//                SharedPreferencesUtil.getInt(context, Constants.UID, -1), sign);
//        return value;
//    }
//
//
//    public static String getToken2(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
//                Context.MODE_PRIVATE);
//        String value = "sign=" + preferences.getString(Constants.TOKEN, "");
//        return value;
//    }
}
