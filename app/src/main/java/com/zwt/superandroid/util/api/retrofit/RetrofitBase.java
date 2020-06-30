package com.zwt.superandroid.util.api.retrofit;

import com.zwt.superandroid.util.api.APIConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by john on 2018/10/16.
 */
public class RetrofitBase<T> {
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String NO_DEFAULT_BODY = "NO_DEFAULT_BODY";
    public static final String CANCELED = "Canceled";
    private T service;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private OkHttpClientInterceptor interceptor;
    private static Class sTargetServiceClz = null;

    private RetrofitBase(Class clz) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor = new OkHttpClientInterceptor());

   /*     Dns dns = HttpDnsUtil.getDns();
        if (dns != null) {
            builder.dns(dns);
        }*/
        builder.sslSocketFactory(SSLUtil.createSSLSocketFactory())
                .hostnameVerifier(new SSLUtil.TrustAllHostnameVerifier());
        okHttpClient = builder.build();
        String baseUrl = APIConfig.BASE;
        if (baseUrl != null && !baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(RequestBodyConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (clz != null) {
            service = (T) retrofit.create(clz);
        }
    }

    private static class RetrofitBaseHolder {
        private static RetrofitBase base = new RetrofitBase(sTargetServiceClz);
    }

    public static <T> T getService(Class<T> cls) {
        sTargetServiceClz = cls;
        return (T) RetrofitBaseHolder.base.service;
    }

    public static OkHttpClient getClient() {
        return RetrofitBaseHolder.base.okHttpClient;
    }

    public static Retrofit getRetrofit() {
        return RetrofitBaseHolder.base.retrofit;
    }

    public static <T> FormBody getFormBody(T value) {
        FormBody.Builder builder = new FormBody.Builder();
    /*    if (value instanceof BaseBody) {
            BaseRequestData data = ((BaseBody) value).getAppDevice();
            if (data != null) {
                List<Header> list = new ArrayList<>();
                list.add(new Header("t", data.getTimestamp()));
                list.add(new Header("deviceno", data.getDeviceId()));

                try {
                    String sign = APIUtils.nativeGetParam(BaseApplication.getContext(), list, null);
                    if (!TextUtils.isEmpty(sign)) {
                        data.setSign(sign);
                    }
                } catch (Throwable r) {
                    r.printStackTrace();
                }
            }
        }

        builder.add("body", new Gson().toJson(value));*/

        return builder.build();
    }
}
