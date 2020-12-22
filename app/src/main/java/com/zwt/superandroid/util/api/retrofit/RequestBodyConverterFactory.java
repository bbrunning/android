package com.zwt.superandroid.util.api.retrofit;


import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RequestBodyConverterFactory extends Converter.Factory {
    public static RequestBodyConverterFactory create() {
        return new RequestBodyConverterFactory();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        return new RequestBodyConverter<>();
    }
}

