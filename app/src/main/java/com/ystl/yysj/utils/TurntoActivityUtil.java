package com.ystl.yysj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.ystl.yysj.base.BaseActivity;
import com.ystl.yysj.ui.h5.activity.NormalH5Activity;
import com.ystl.yysj.ui.h5.bean.WebBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/1.
 */
public class TurntoActivityUtil {
    public static final String BEAN = "bean";
    public static final String beanKey = "bean";

    /**
     * 页面跳转 工具
     *
     * @param context  context
     * @param toClass      跳转到的Activity
     * @param bean         传递的对象  不传为null
     */
    public static void turnToNormalActivity(Context context, Class toClass, Serializable bean) {
        Intent intent = new Intent();
        intent.setClass(context, toClass);
        if (bean != null) {
            intent.putExtra(beanKey, bean);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//防止 Calling startActivity() from outside of an Activity context
        context.startActivity(intent);
    }

    public static void turnToNormalActivityF(BaseActivity baseActivity, Class toClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(baseActivity, toClass);

        baseActivity.startActivityForResult(intent, requestCode);
    }
    public static void turnToNormalActivityF(BaseActivity baseActivity, Class toClass, Serializable bean, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(baseActivity, toClass);
        if (bean != null) {
            intent.putExtra(beanKey, bean);
        }
        baseActivity.startActivityForResult(intent, requestCode);
    }

    public static void turnToNormalActivityF(BaseActivity baseActivity, Class toClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(baseActivity, toClass);
        if (bundle != null) {
            intent.putExtra(beanKey, bundle);
        }
        baseActivity.startActivityForResult(intent, requestCode);
    }

    public static void turnToNormalActivityWithB(Context context, Class toClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, toClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到 普通的webview 界面 只需要传一个 WebBean
     *
     * @param context
     * @param bean
     */
    public static void turnToNormalWebActivity(Context context, WebBean bean) {
        Intent intent = new Intent();
        intent.setClass(context, NormalH5Activity.class);
        if (bean != null) {
            intent.putExtra(BEAN, bean);
        }
        context.startActivity(intent);
    }

    /**
     * 从相册选择照片并裁剪
     *
     * @param mContext
     */
    public static void turnPhotosWithCrop(Activity mContext, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        mContext.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照
     */
    public static void turnToCapture(Activity mContext, int requestCode, Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        mContext.startActivityForResult(intent, requestCode);//or TAKE_SMALL_PICTURE
    }

    /**
     * 拍照
     */
    public static void turnToCapture1(Activity mContext, int requestCode, Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mContext.startActivityForResult(intent, requestCode);//or TAKE_SMALL_PICTURE
    }
}
