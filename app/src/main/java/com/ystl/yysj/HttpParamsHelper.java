package com.ystl.yysj;

/**
 * Created by zhangdi on 17/4/11.
 */
public class HttpParamsHelper {
    private HttpParamsHelper() {
    }

    public static HttpParamsHelper getInstance() {
        return HttpParamsHelperHolder.INSTANCE;
    }

    private static class HttpParamsHelperHolder {
        private static final HttpParamsHelper INSTANCE = new HttpParamsHelper();
    }
}
