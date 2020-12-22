package com.zwt.superandroid.util.api.retrofit;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Converter;
public class RequestBodyConverter<T> implements Converter<T, RequestBody> {
    @Override
    public RequestBody convert(T value) throws IOException {
        return RetrofitBase.getFormBody(value);
    }
}