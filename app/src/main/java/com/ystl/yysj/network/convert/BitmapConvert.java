package com.ystl.yysj.network.convert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：字符串的转换器
 * 修订历史：
 * ================================================
 */
public class BitmapConvert implements Converter<Bitmap> {

    public static BitmapConvert create() {
        return ConvertHolder.convert;
    }

    @Override
    public Bitmap convertSuccess(Response value) throws Exception {
        return BitmapFactory.decodeStream(value.body().byteStream());
    }

    private static class ConvertHolder {
        private static BitmapConvert convert = new BitmapConvert();
    }
}