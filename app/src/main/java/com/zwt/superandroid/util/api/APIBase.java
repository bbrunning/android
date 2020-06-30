package com.zwt.superandroid.util.api;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/8
 * Time: 19:51
 */
public class APIBase {
    public interface ResponseListener<T> {
        void onSuccess(T data, String result, String code, String msg, boolean isBusinessSuccess);

        void onFailure(int code, String result);
    }
}
