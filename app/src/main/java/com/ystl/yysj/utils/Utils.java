package com.ystl.yysj.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ystl.yysj.App;
import com.ystl.yysj.R;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangdi on 16/11/2.
 */
public class Utils {
    private static long lastClickTime;

    /**
     * 判断是否是快速点击，时间间隔800ms
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 根据包明查找 应用版本号
     *
     * @param context
     * @param packageName
     * @return -1没有该应用
     */
    public static int getAppVersionCodeByPackageName(Context context, String packageName) {
        int versionCode = -1;
        if (context != null) {
            PackageManager pkgMag = context.getPackageManager();
            List<PackageInfo> infoList = pkgMag.getInstalledPackages(0);
            for (PackageInfo info : infoList) {
                if (info.packageName.equals(packageName)) {
                    versionCode = info.versionCode;
                    break;
                }
            }
        }

        return versionCode;
    }


    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    public static int getAPPVersionCodeFromAPP(Context ctx) {
        int currentVersionCode = 0;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch blockd
            e.printStackTrace();
        }
        return currentVersionCode;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String version = info.versionName;
            return ctx.getString(R.string.version_name) + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context mContext) {
        boolean flage = false;
        try {
            ConnectivityManager cManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cManager.getAllNetworkInfo() != null) {
                flage = cManager.getActiveNetworkInfo().isConnected();
            }
        } catch (Exception e) {

        }

        return flage;
    }

    /**
     * 判断SD卡是否插入 即是否有SD卡
     */
    public static boolean isSDCardMounted() {
        return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState());
    }

    public static String getYearAndMonth(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        int index = time.length();
        if (time.contains(":")) {
            index = time.lastIndexOf(":");
        }
        return time.substring(0, index);
    }

    /**
     * 控制软键盘
     *
     * @param et
     * @param isShow 是否是显示关键盘
     */
    public static void contolSoftKeyBoard(EditText et, boolean isShow) {
        InputMethodManager inputManager =
                (InputMethodManager) et.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            inputManager.showSoftInput(et, 0);
        } else {
            inputManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }


    /**
     * 格式化价格  保留两位小数点
     *
     * @param d
     * @return
     */
    public static String formatPrice(double d) {
        return new java.text.DecimalFormat("############0.00").format(d);
    }

    public static String formatPrice(String str) {

        if (!TextUtils.isEmpty(str)) {
            double price = 0;
            try {
                price = Double.parseDouble(str);
            } catch (Exception e) {

            }
            return new java.text.DecimalFormat("############0.00").format(price);
        }
        return "0.00";
    }

    /**
     * 将list集合转为以逗号分隔的字符串
     *
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i) + ",");
        }
        String ids = new String(builder);
        String ids2 = "";
        if (ids.length() > 0) {
            ids2 = ids.substring(0, ids.lastIndexOf(","));
        }
        return ids2;
    }


    /**
     * 13213213213 => 132****1231
     *
     * @param phone
     * @return
     */
    public static String formatPhoneNumber(String phone) {
        String s = phone.trim();
        if (!TextUtils.isEmpty(s) && s.length() >= 8) {
            return s.substring(0, 3) + "****" + s.substring(7, s.length());
        }

        return s;
    }

    /**
     * 检查 是否有相机
     *
     * @param context
     * @return
     */
    public static boolean checkCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
                || Camera.getNumberOfCameras() > 0;
        return hasACamera;
    }

    // 是否有打电话模块
    public static boolean isTabletDevice() {
        TelephonyManager telephony = (TelephonyManager) App.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        return type == TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 时间转化  2017-02-15 13:12:54 》 2017年02月15日 13:12
     */
    public static String getDate(String s) {
        String newString = s;
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        try {
            Date date = format1.parse(s);
            newString = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newString;
    }

    /**
     * 获取 x小时x时间
     *
     * @param second
     * @return
     */
    public static String getMinute(String second) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        long sec = Long.parseLong(second);
        long hour = sec / hh;
        long minute = (sec - hour * hh) / mi;

        if (hour > 0) {
            if (minute > 0) {
                return hour + "小时" + minute + "分钟";
            } else {
                return hour + "小时";
            }
        } else {
            return (minute == 0 ? 1 : minute) + "分钟";
        }
    }

    /**
     * list 转 string 加分隔符
     *
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List list, char separator) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String formatTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            if (time.contains("T")) {
                time = time.replace("T", " ");
            }
        }
        return time;
    }

    /**
     * decimal 加减时候，参数要转成String
     *
     * @param double1
     * @param double2
     * @return
     */
    public static double decimalAdd(double double1, double double2) {

        BigDecimal bigDouble1 = new BigDecimal(Double.toString(double1));
        BigDecimal bigDouble2 = new BigDecimal(Double.toString(double2));

        return bigDouble1.add(bigDouble2).doubleValue();
    }

    public static double decimalSubtract(double doubles1, double doubles2) {
        BigDecimal sub = new BigDecimal(Double.toString(doubles1));
        BigDecimal b1 = new BigDecimal(Double.toString(doubles2));
        return sub.subtract(b1).doubleValue();
    }

    // 进行乘法运算
    public static double decimalMul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();
    }

    // 进行除法运算
    public static double decimalDiv(double d1, double d2, int len) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
