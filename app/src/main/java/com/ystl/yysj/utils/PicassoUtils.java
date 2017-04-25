package com.ystl.yysj.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.ystl.yysj.network.utils.SimpleTarget;

/**
 * Picasso封装的方法
 */

public class PicassoUtils {

    public static void loadImage(Context context, String imgUrl, ImageView iv) {
        if (!TextUtils.isEmpty(imgUrl))
            Picasso.with(context).load(getImage(imgUrl)).into(iv);
    }

    /***
     * 给非ImageView设置背景图片
     *
     * @param context
     * @param imgUrl
     * @param target
     */
    public static void loadImage(Context context, String imgUrl, SimpleTarget target) {
        if (!TextUtils.isEmpty(imgUrl))
            Picasso.with(context).load(getImage(imgUrl)).into(target);
    }

    public static void loadImage(Context context, String imgUrl, Target target) {
        if (!TextUtils.isEmpty(imgUrl))
            Picasso.with(context).load(getImage(imgUrl)).into(target);
    }

    /**
     * 指定大小加载图片
     *
     * @param mContext   上下文
     * @param imgUrl     图片路径
     * @param width      宽
     * @param height     高
     * @param mImageView 控件
     */
    public static void loadImageWithSize(Context mContext, String imgUrl, int width, int height, ImageView mImageView) {
        if (!TextUtils.isEmpty(imgUrl))
            Picasso.with(mContext).load(getImage(imgUrl)).resize(width, height).centerCrop().into(mImageView);
    }

    /**
     * 加载有默认图片
     *
     * @param mContext   上下文
     * @param imgUrl     图片路径
     * @param resId      默认图片资源
     * @param mImageView 控件
     */
    public static void loadImageWithHolder(Context mContext, String imgUrl, int resId, ImageView mImageView) {
        Picasso.with(mContext).load(getImage(imgUrl)).fit().placeholder(resId).into(mImageView);
    }

    /**
     * 裁剪图片
     *
     * @param mContext   上下文
     * @param imgUrl     图片路径
     * @param mImageView 控件
     */
    public static void loadImageWithCrop(Context mContext, String imgUrl, ImageView mImageView) {
        Picasso.with(mContext).load(getImage(imgUrl)).transform(new CropImageView()).into(mImageView);
    }
//
//    public static void loadInto(String imgUrl, ImageView imageView) {
//        singleton.load(getImage(imgUrl)).into(imageView);
//    }
//
//
//    public static void loadInto(String imgUrl, Target target) {
//        singleton.load(getImage(imgUrl)).into(target);
//    }

    /**
     * 自定义图片裁剪
     */
    public static class CropImageView implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap newBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (newBitmap != null) {
                //内存回收
                source.recycle();
            }
            return newBitmap;
        }

        @Override
        public String key() {
            return "lgl";
        }
    }

    public static String getImage(String image) {
        return getImage(image, 1);
    }

    /**
     * @return
     */
    public static String getImage(String image, int type) {
        String s = image;
//        if (!TextUtils.isEmpty(image) && !s.startsWith("http")) {
//            if (BuildConfig.DEBUG) {
//                s = App.getInstance().getImageUrl() + s;
//            } else {
//                s = BuildConfig.xs_Image + s;
//            }
//        }
        return s;
    }
}
