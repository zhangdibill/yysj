package com.ystl.yysj.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.ystl.yysj.App;
import com.ystl.yysj.AppUrls;
import com.ystl.yysj.HttpParamsHelper;
import com.ystl.yysj.base.BaseResponse;
import com.ystl.yysj.base.JsonCallback;
import com.ystl.yysj.network.OkGo;
import com.ystl.yysj.ui.login.bean.UserInfo;

import okhttp3.Call;
import okhttp3.Response;

public class AutoLoginService extends Service {

    private static final int TASK_FINISH = 1022;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TASK_FINISH) {
                Log.e("自动登录服务结束","-----");
                stopSelf();
            }
            super.handleMessage(msg);
        }
    };

    public AutoLoginService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        login();
        return super.onStartCommand(intent, flags, startId);
    }

    private void login() {
        final UserInfo userInfo = App.getInstance().getUserInfo();
        if (userInfo != null){
//            String json = HttpParamsHelper.getInstance().autoLogin(userInfo.sm_saleman_id,userInfo.token);
            String json = "";
            OkGo.post(AppUrls.getInstance().LOGIN_URL).tag(this).upJson(json).execute(new JsonCallback<BaseResponse<UserInfo>>() {
                @Override
                public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse, Call call, Response response) {

                    int code = userInfoBaseResponse.content.businessCode;
                    switch (code) {
                        case 1://验证验证码成功，
                            UserInfo info = userInfoBaseResponse.content.data;
                            App.getInstance().setUserInfo(info);
                            break;
                        default:

                            break;
                    }

                    finishTask();

                }
            });
        } else {
            finishTask();
        }
    }

    private void finishTask() {
        Message message = Message.obtain();
        message.what = TASK_FINISH;
        mHandler.sendMessage(message);
    }
}
