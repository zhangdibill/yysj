
package com.ystl.yysj.network;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.ystl.yysj.App;
import com.ystl.yysj.R;
import com.ystl.yysj.network.utils.DialogUtils;
import com.ystl.yysj.utils.Utils;
import com.ystl.yysj.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Response;

public class NetUtils {

    //wap网络汇总
    public static final String CTWAP = "ctwap";
    public static final String CMWAP = "cmwap";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final String REQUEST_CONTENT = "content";
    public static final String REQUEST_MSG = "msg";
    public static final String REQUEST_CODE = "code";

    public static final String REQUEST_BUSINESSCODE = "businessCode";
    public static final String REQUEST_MESSAGE = "message";
    public static final String REQUEST_DATA = "data";

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null) {
                return activeNetInfo.getTypeName();
            } else {
                return "other";
            }
        } catch (Exception e) {
            return "other";
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {

        boolean flag = false;

        try {
            ConnectivityManager cwjManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cwjManager.getActiveNetworkInfo() != null)
                flag = cwjManager.getActiveNetworkInfo().isConnected();//.isAvailable();
        } catch (Exception e) {

        }

        return flag;
    }

    /**
     * 判断当前网络是否是wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前网络是否是wap
     *
     * @param context
     * @return
     */
    public static boolean isCMWAPMobileNet(Context context) {

        if (null == context) {
            return false;
        }

        if (isWifi(context)) {
            return false;
        } else {
            ConnectivityManager mag = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (null != mag) {
                NetworkInfo mobInfo = mag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (null != mobInfo) {
                    String extrainfo = mobInfo.getExtraInfo();
                    if (null != extrainfo) {
                        extrainfo = extrainfo.toLowerCase();
                        return extrainfo.contains("wap");
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

    }


    /**
     * 获取wap网络条件下的代理服务器，如果为非wap返回为空对象
     *
     * @param context
     * @return
     */
    public static String getHostbyWAP(Context context) {

        if (null == context) {
            return null;
        }

        if (isWifi(context)) {
            return null;
        }

        try {
            String result = null;
            ConnectivityManager mag = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != mag) {
                NetworkInfo mobInfo = mag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (null != mobInfo) {

                    String extrainfo = mobInfo.getExtraInfo();
                    if (null != extrainfo) {
                        extrainfo = extrainfo.toLowerCase();
                        if (extrainfo.equals(CMWAP) || extrainfo.equals(WAP_3G) || extrainfo.equals(UNIWAP)) {//移动 or 联通wap代理
                            result = "10.0.0.172";
                        } else {
                            //电信WAP判断
                            Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
                            final Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
                            if (c != null) {
                                c.moveToFirst();
                                final String user = c.getString(c.getColumnIndex("user"));
                                if (!TextUtils.isEmpty(user)) {
                                    if (user.toLowerCase().startsWith(CTWAP)) {
                                        result = "10.0.0.200";
                                    }
                                }
                                c.close();
                            }

                        }
                    }
                }
            }

            return result;
        } catch (Exception e) {
            return null;
        }

    }

    //IP正则表达式判断
    public static boolean isboolIp(String ipAddress) {

        try {
            String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            Pattern pattern = Pattern.compile(ip);
            Matcher matcher = pattern.matcher(ipAddress);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 设置proxy 在systemProperties中设置参数
     *
     * @param proxy
     * @param port
     */
    public static void setHttpProxy(String proxy, String port) {
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("http.proxyHost", proxy);
        systemProperties.setProperty("http.proxyPort", port);
    }

    /**
     * 从url获取含有key的参数值
     *
     * @param url
     * @param key
     * @return
     */
    public static String parseURL(String url, String key) {

        String rst = "";

        if (null == key || "".equalsIgnoreCase(key)) {

        } else {

            int i = url.indexOf("?");
            if (i + 1 <= url.length()) {

                String param = url.substring(i + 1, url.length());
                String[] values = param.split("&");
                if (null != values) {

                    for (int index = 0; index < values.length; index++) {

                        if (values[index].contains(key)) {

                            String[] keys = values[index].split("=");

                            if (keys.length > 1) {
                                rst = keys[1];
                                break;
                            }
                        }
                    }
                }

            }
        }

        return rst;
    }

    /**
     * 获得URL的host
     *
     * @param url
     * @return
     */
    public static String getHost(String url) {
        if (null == url || "".equals(url)) {
            return "";
        } else {
            try {
                String newurl = url.replace("http://", "");
                newurl = newurl.substring(0, newurl.indexOf("/"));
                return newurl;
            } catch (Exception e) {
                return "";
            }
        }
    }

    public static String getContentBySuccess(String str) throws JSONException {
        return getContentBySuccess(str, false);
    }


    public static String getContentJosn(String json) {
        String content = "";
//        {"code":200,"msg":"","content":{"businessCode":2,"message":"未查询到已绑定的银行卡信息","data":""}}
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(json);
        com.alibaba.fastjson.JSONObject contentObject = jsonObject.getJSONObject("content");
        return contentObject.toJSONString();
    }

    public static String getContentList(String json) {
        String contentSucess = null;
        JSONArray array = null;
        try {
            contentSucess = getContentBySuccess(json);

            JSONObject jsonObject = new JSONObject(contentSucess);
            array = jsonObject.getJSONArray("resultList");
            if (array != null && array.length() > 0) {
                return array.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getContentListTotalPage(String json) {
        String contentSucess = null;
        int totalPage = 0;
        JSONArray array = null;
        try {
            contentSucess = getContentBySuccess(json);

            JSONObject jsonObject = new JSONObject(contentSucess);
            totalPage = jsonObject.optInt("totalPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalPage;
    }

    /**
     * 获取api请求的数据
     *
     * @param str
     * @param isShow
     * @return
     * @throws JSONException
     */
    public static String getContentBySuccess(String str, boolean isShow) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        JSONObject jsonObject = new JSONObject(str);
        int code = jsonObject.optInt(REQUEST_CODE);
        RequestContentHolder.requestContent.setCode(code);
        if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT) {
            JSONObject content = jsonObject.optJSONObject(REQUEST_CONTENT);
            if (content != null) {
                RequestContentHolder.requestContent.setBusimessCode(content.optInt(REQUEST_BUSINESSCODE));
                RequestContentHolder.requestContent.setMessage(content.optString(REQUEST_MESSAGE));

                Object obj = content.opt(REQUEST_DATA);
                if (obj instanceof JSONObject) {
                    RequestContentHolder.requestContent.setData(
                            content.optJSONObject(REQUEST_DATA).toString());

                    return RequestContentHolder.requestContent.getData();
                } else if (obj instanceof JSONArray) {
                    RequestContentHolder.requestContent.setData(
                            content.optJSONArray(REQUEST_DATA).toString());

                    return RequestContentHolder.requestContent.getData();
                }
            } else {
                JSONObject body = jsonObject.optJSONObject("body");
                if (body != null) {
                    return body.toString();
                }
            }
        } else {
            if (isShow &&
                    !TextUtils.isEmpty(jsonObject.optString(REQUEST_MSG))) {
                // TODO   BBBBBBB   显示请求返回信息
                Toast.makeText(App.getInstance().getApplicationContext(), jsonObject.optString(REQUEST_MSG), Toast.LENGTH_SHORT).show();
            }

            if (code == HttpURLConnection.HTTP_SERVER_ERROR) {
                Toast.makeText(App.getInstance().getApplicationContext(), jsonObject.optString(REQUEST_MSG), Toast.LENGTH_SHORT).show();
            }

            return str;
        }

        return str;
    }


    /**
     * 业务请求msg
     *
     * @param str
     * @return
     * @throws JSONException
     */
    public static String getRequsetMsg(String str) throws JSONException {
        try {
            if (!TextUtils.isEmpty(str)) {

                JSONObject jsonObject = new JSONObject(str);
                int code = jsonObject.optInt(REQUEST_CODE);
                if (code == 200) {
                    return new JSONObject(str).optJSONObject(REQUEST_CONTENT).
                            optString(REQUEST_MESSAGE);
                } else if (code == HttpURLConnection.HTTP_SERVER_ERROR) {
                    return "";
                } else {
                    return new JSONObject(str).optJSONObject(REQUEST_CONTENT).
                            optString(REQUEST_MESSAGE);
                }
            }
        } catch (NullPointerException e) {
            return "";
        }


        return RequestContentHolder.requestContent.getMessage();
    }

    /**
     * Request Code
     *
     * @param str
     * @return
     * @throws JSONException
     */
    public static int getRequsetCode(String str) throws JSONException {
        if (!TextUtils.isEmpty(str)) {
            return new JSONObject(str).optInt(REQUEST_CODE);
        }

        return RequestContentHolder.requestContent.getCode();
    }

    /**
     * businessCode
     *
     * @return
     */
    public static int getBusinessCode(String jsonStr) throws JSONException {
        if (!TextUtils.isEmpty(jsonStr)) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int code = jsonObject.optInt(REQUEST_CODE);
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT) {
                return new JSONObject(jsonStr).optJSONObject(REQUEST_CONTENT).
                        optInt(REQUEST_BUSINESSCODE);
            } else if (code == HttpURLConnection.HTTP_SERVER_ERROR) {
                Toast.makeText(App.getInstance().getApplicationContext(), jsonObject.optString(REQUEST_MSG),
                        Toast.LENGTH_SHORT).show();
                return 500;
            }
        }

        return RequestContentHolder.requestContent.getBusimessCode();
    }

    /**
     * 获取网络类型 2G/3G/4G/wifi/unknow
     *
     * @param context
     * @return
     */
    public static String getNetworkTypeClass(@NonNull Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        int netWorkType = telephonyManager.getNetworkType();
        switch (netWorkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }

    }

    /**
     * 获取运营商信息
     * 中国移动00
     * 中国联通11
     * 中国电信03
     *
     * @param context
     * @return
     */
    public static String getServiceProvider(@NonNull Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        String subscribeId = telephonyManager.getSubscriberId();
        String ProvidersName = null;
        if (!TextUtils.isEmpty(subscribeId)) {
            if (subscribeId.startsWith("46000") || subscribeId.startsWith("46002")) {
                ProvidersName = "00";//中国移动
            } else if (subscribeId.startsWith("46001")) {
                ProvidersName = "01";//中国联通
            } else if (subscribeId.startsWith("46003")) {
                ProvidersName = "03";//中国电信
            }
        }

        return ProvidersName;
    }

    public static void processNetError(Context context, Response response, String body) {
        DialogUtils.hideDialogForLoading();
        if (context == null) {
            return ;
        }
        if (!Utils.isNetworkConnected(context)) {
            ToastUtil.showToast(context, context.getString(R.string.no_internet));
        } else if (response != null) {
            if (response.code() == 400) {
                handle400Error(context, response, body);
            } else if (response.code() == 500) {
                ToastUtil.showToast(context, context.getString(R.string.error));
            }
        } else {
            ToastUtil.showToast(context, context.getString(R.string.error));
        }

    }

    // 400 单点处理
    public static void handler400(Context context) {
//        TurntoActivityUtil.turnToNormalActivity(context, LoginActivity.class, null);
//        App.getInstance().exitApp(true);
//        AppDataBaseHelper.getInstance(context).delUsrInfo(App.getInstance().getUserInfo());
//        App.getInstance().setUserInfo(null);
    }

    /**
     * 400 单点登录处理
     */
    private static void handle400Error(Context context, Response response, String content) {

//        TurntoActivityUtil.turnToNormalActivity(context, LoginActivity.class, null);
//        App.getInstance().exitApp(true);
//        AppDataBaseHelper.getInstance(context).delUsrInfo(App.getInstance().getUserInfo());
//        App.getInstance().setUserInfo(null);
//        BaseResponse<String> response1 = new BaseResponse<>();
//        BaseResponse baseResponse = null;
//        if (content != null) {
//            baseResponse = JSON.parseObject(content, response1.getClass());
//        } else {
//            try {
//                baseResponse = JSON.parseObject(response.body().string(), response1.getClass());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (baseResponse != null) {
//            ToastUtil.showToast(context, baseResponse.msg);
//        }
    }

    private static class RequestContentHolder {
        private static RequestContent requestContent = new RequestContent();
    }
}
