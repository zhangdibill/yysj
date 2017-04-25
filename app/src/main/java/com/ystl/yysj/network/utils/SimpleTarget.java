package com.ystl.yysj.network.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by LXH on 17/2/24.
 */

public class SimpleTarget implements Target {

    onSimpleBitLoadedListener onSimpleListener;

    public SimpleTarget(onSimpleBitLoadedListener onSimpleListener) {
        this.onSimpleListener = onSimpleListener;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        if (onSimpleListener != null) {
            onSimpleListener.onBitmapLoad(bitmap);
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }


    public interface onSimpleBitLoadedListener {
        void onBitmapLoad(Bitmap bitmap);
    }

}
