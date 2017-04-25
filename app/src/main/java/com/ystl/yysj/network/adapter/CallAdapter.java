package com.ystl.yysj.network.adapter;

/**
 * 返回值的适配器
 */
public interface CallAdapter<T> {

    /**
     * call执行的代理方法
     */
    <R> T adapt(Call<R> call);
}