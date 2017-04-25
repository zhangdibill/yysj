package com.ystl.yysj;

/**
 * Created by zhangdi on 17/4/11.
 */
public class AppUrls {
    private AppUrls() {
    }

    public static AppUrls getInstance() {
        return AppUrlsHolder.INSTANCE;
    }

    private static class AppUrlsHolder {
        private static final AppUrls INSTANCE = new AppUrls();
    }

    public static String LOGIN_URL = "";
}
