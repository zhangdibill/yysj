package com.ystl.yysj.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Content<T> implements Serializable {
    public int businessCode;
    public String message;
    public T data;

    @Override
    public String toString() {
        return "Content{" +
                "businessCode=" + businessCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
