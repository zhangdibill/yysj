package com.ystl.yysj.network.utils;

import android.content.Context;
import android.os.Environment;

import com.ystl.yysj.Constants;

import java.io.File;

/**
 * Created by zhangdi on 2015/8/24.
 */
public class FileUtil {

    public static boolean isSDcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static File getDiskCacheDir(Context context) {
        final String cachePath = isSDcardExist() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
//        return cachePath + File.separator + uniqueName);
        return new File(cachePath);
    }

    public static File getExternalCacheDir(Context context) {
        final String cacheDir = Constants.DOWNLOAD_FILE;
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
}
