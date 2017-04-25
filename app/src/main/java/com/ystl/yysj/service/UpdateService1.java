package com.ystl.yysj.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ystl.yysj.R;
import com.ystl.yysj.network.NetUtils;
import com.ystl.yysj.network.OkGo;
import com.ystl.yysj.network.callback.FileCallback;
import com.ystl.yysj.network.utils.FileUtil;
import com.ystl.yysj.utils.SharedPreferencesUtil;
import com.ystl.yysj.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 自己写更新下载apk
 * Created by zhangdi on 16/10/13.
 */
public class UpdateService1 extends Service {

    private static final int DOWN_OK = 1; // 下载完成
    private static final int DOWN_ERROR = 0;
    private static final String apkName = "zhtxcsywy";
    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K
    private static String down_url; // = "http://192.168.1.112:8080/360.apk";
    private static long lastbytes;
    long totalSize = 0;// 文件总大小
    /**
     * 创建通知栏
     */
    RemoteViews contentView;
    private String app_name;
    private NotificationManager notificationManager;
    private Notification notification;
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private File updateFile;
    private int notification_id = 11;
    /***
     * 更新UI
     */
    final Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            SharedPreferencesUtil.putBoolean(getApplicationContext(), "candel", true);
            switch (msg.what) {
                case DOWN_OK:
                case DOWN_ERROR:
                    notificationManager.cancel(notification_id);
                    stopSelf();
                    break;
                default:
                    stopSelf();
                    break;
            }
        }
    };

    /**
     * 下载完成后打开安装apk界面
     */
    public static void installApk(File file, Context context) {

        Log.i("BBBB", "file = " + file + "   file.exists() = " + file.exists());

        if (file.exists()) {
            openFile(file, context);
        }

    }

    public static void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setAction("android.intent.action.VIEW");
        String var3 = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), var3);
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent getFileIntent(File file) {
        Uri uri = Uri.fromFile(file);
        String fName = file.getName();
        String type;
        // 取得扩展名
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length());
        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            try {
                app_name = intent.getStringExtra("app_name");
                down_url = intent.getStringExtra("downloadurl");

                boolean b = !SharedPreferencesUtil.getBoolean(getApplicationContext(), "candel");

                if (b) {
                    refreshFileList();
                }

                // 创建文件
                File updateFile = FileUtil.getDiskCacheDir(getApplicationContext());
                boolean creatFile = (updateFile.exists() && updateFile.isDirectory()) ? true : updateFile.mkdirs();
                if (creatFile) {
                    // 创建通知
                    createNotification();
                    // 开始下载
//                    downloadUpdateFile(down_url, updateFile);
                    downLoadApk(down_url, updateFile);
                } else {
                    ToastUtil.showToast(this, "创建下载目录失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToast(this, "下载失败");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 删除之前下载的老的apk
     */
    private void refreshFileList() {
        File path = FileUtil.getDiskCacheDir(getApplicationContext());
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                Log.i("BBBB", "该目录下没有任何一个文件！");
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    String strFileName = files[i].getAbsolutePath().toLowerCase();
                    Log.i("BBBB", "strFileName = " + strFileName);
                    if (strFileName.contains("apk/" + apkName)) {
                        Log.i("BBBB", "正在删除：" + strFileName);
                        files[i].delete();
                    }
                }
            }
        }

    }

    @SuppressWarnings("deprecation")
    public void createNotification() throws Exception {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        // 这个参数是通知提示闪出来的值.
        notification.tickerText = "开始下载";

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);

        contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        notification.contentView = contentView;
        notificationManager.notify(notification_id, notification);
    }

    /**
     * 下载客户端文件
     */
    private void downLoadApk(String url, File file) {
        SharedPreferencesUtil.putBoolean(getApplicationContext(), "candel", false);
        Log.i("BBBB", "down_url = " + down_url);

        String fileName = apkName + System.currentTimeMillis() + ".apk";
        String fileDir = file.getAbsolutePath();
        OkGo.get(url).tag(this).execute(new FileCallback(fileDir, fileName) {
            @Override
            public void onSuccess(File file, Call call, Response response) {
                installApk(file, UpdateService1.this);

                Message message = handler.obtainMessage();
                message.what = DOWN_OK;
                handler.sendMessage(message);

            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                String downLength = Formatter.formatFileSize(getApplicationContext(), currentSize);
//                String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
//                String speed = Formatter.formatFileSize(getApplicationContext(), networkSpeed);

                contentView.setTextViewText(R.id.notificationPercent, (Math.round(progress * 10000) * 1.0f / 100) + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, (int) (progress * 100), false);
                notificationManager.notify(notification_id, notification);

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                NetUtils.processNetError(getApplicationContext(), response, null);

                Message message = handler.obtainMessage();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }
        });
    }
}
