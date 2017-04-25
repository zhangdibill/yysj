package com.ystl.yysj.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/3.
 */
public class BaseResponse<T> implements Serializable {
    public int code;
    public String msg;
    public Content<T> content;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
