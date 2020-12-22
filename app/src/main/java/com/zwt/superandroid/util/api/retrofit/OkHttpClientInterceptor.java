package com.zwt.superandroid.util.api.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpClientInterceptor implements Interceptor {
    //修改重试次数, 切换域名，每个域名重试一次
    private static final int DEFAULT_MAX_RETRIES = 2;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response rsp = chain.proceed(request);
        return rsp;

    }
}
