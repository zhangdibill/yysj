package com.ystl.yysj.network.callback;

import com.ystl.yysj.network.convert.StringConvert;
import com.ystl.yysj.network.request.BaseRequest;

import okhttp3.Response;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：返回字符串类型的数据
 * 修订历史：
 * ================================================
 */
public abstract class StringCallback extends AbsCallback<String> {

    public String s;

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
//        //主要用于在所有请求之前添加公共的请求头或请求参数，例如登录授权的 token,使用的设备信息等,可以随意添加,也可以什么都不传
//        UserInfo userInfo = App.getInstance().getUserInfo();
//        if (userInfo != null) {
//            request.headers("sign", "userId=" + userInfo.sm_saleman_id + ";token=" + userInfo.token);
//        }
    }

    @Override
    public String convertSuccess(Response response) throws Exception {
        s = StringConvert.create().convertSuccess(response);
        response.close();
        return s;
    }
}