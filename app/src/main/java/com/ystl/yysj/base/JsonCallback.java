package com.ystl.yysj.base;


import com.alibaba.fastjson.JSON;
import com.ystl.yysj.network.callback.AbsCallback;
import com.ystl.yysj.network.request.BaseRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    public String body;

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //主要用于在所有请求之前添加公共的请求头或请求参数，例如登录授权的 token,使用的设备信息等,可以随意添加,也可以什么都不传
//        UserInfo userInfo = App.getInstance().getUserInfo();
//        if (userInfo != null) {
//            request.headers("sign", "userId=" + userInfo.sm_saleman_id + ";token=" + userInfo.token);
//        }
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertSuccess(Response response) throws Exception {

        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        body = response.body().string();
        T t = JSON.parseObject(body, params[0]);
        response.close();
        return t;
    }
}