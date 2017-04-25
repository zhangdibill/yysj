package com.ystl.yysj.network;

/**
 * 网络数据返回信息
 * Created by jinpeng on 15/10/23.
 */
public class RequestContent {
    private int code;
    private int busimessCode;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getBusimessCode() {
        return busimessCode;
    }

    public void setBusimessCode(int busimessCode) {
        this.busimessCode = busimessCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
