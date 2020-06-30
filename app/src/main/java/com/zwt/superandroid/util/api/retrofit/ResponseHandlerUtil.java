package com.zwt.superandroid.util.api.retrofit;

import com.google.gson.Gson;
import com.zwt.superandroid.util.LogUtil;
import com.zwt.superandroid.util.api.APIBaseRequest;
import com.zwt.superandroid.util.api.APIBaseResponse;
import com.zwt.superandroid.util.api.BaseResponseData;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/12
 * Time: 16:38
 */
public class ResponseHandlerUtil {
    private static final String TAG = ResponseHandlerUtil.class.getSimpleName();

    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    private static <T extends BaseResponseData> APIBaseResponse<T> getResultResponse(APIBaseRequest<T> request, String result) {
        try {
            APIBaseResponse<T> resultResponse;
            Type type = request.getClass().getGenericSuperclass();
            if (type == null) {
                return null;
            }
            Type[] transformTypeArray = ((ParameterizedType) type).getActualTypeArguments();
            if (transformTypeArray.length == 0) {
                return null;
            }
            Type targetType = transformTypeArray[0];
            Type objectType = type(APIBaseResponse.class, targetType);
            resultResponse = new Gson().fromJson(result, objectType);
            return resultResponse;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends BaseResponseData> void parseResponse(Call call, Response response, String result, APIBaseRequest<T> request) {
        //如果回调为空则return
        if (request.getRspListener() == null) {
            return;
        }
        if (response.isSuccessful()) {
            APIBaseResponse<T> resultResponse = getResultResponse(request, result);
            if (resultResponse != null && resultResponse.getData() != null) {
                request.getRspListener().onSuccess(resultResponse.getData(), result, resultResponse.getErrorCode() + "", resultResponse.getErrorMsg(), true);
            } else {
                LogUtil.d(TAG, "数据解析失败");
                parseFailure(call, response, result, request, null);
            }
        } else {
            parseFailure(call, response, result, request, null);
        }
    }

    public static <T extends BaseResponseData> void parseFailure(Call call, Response response,
                                                                 String result,
                                                                 APIBaseRequest<T> request,
                                                                 IOException exception) {
        if (request != null) {
            request.getRspListener().onFailure(0, "");
        }


    }

}
