package com.zwt.superandroid.util.api.retrofit;

import android.os.Message;

import com.zwt.superandroid.util.LogUtil;
import com.zwt.superandroid.util.WeakHandler;
import com.zwt.superandroid.util.api.APIBaseRequest;
import com.zwt.superandroid.util.api.BaseResponseData;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/11
 * Time: 18:35
 */
public final class OkHttpRequestUtil implements WeakHandler.MessageListener {

    private static final String TAG = OkHttpRequestUtil.class.getSimpleName();

    private boolean isInThread;
    private APIBaseRequest<?> mRequest;
    private Response mResponse;
    private WeakHandler mHandler;
    private Call mCall;
    private String mResult;
    private IOException mException;

    public OkHttpRequestUtil() {
    }

    public OkHttpRequestUtil(boolean isInThread) {
        this.isInThread = isInThread;
    }

    public <T extends BaseResponseData> void request(Call call, APIBaseRequest<T> request) {
        mRequest = request;
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i(TAG, "API请求失败" + call.request().url() + "\n " +
                        call.request().body());
                mCall = call;
                mException = e;
                mResult = null;
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(ResultCode.RESULT_FAIL);
                } else {
                    try {
                        ResponseHandlerUtil.parseFailure(call, null, null, request, e);
                    } catch (Throwable r) {
                        r.printStackTrace();
                    }
                }
                if (request.getController() != null) {
                    request.getController().dismiss();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mCall = call;
                mResponse = response;
                mResult = response.body().string();
                LogUtil.i(TAG, "成功" + mResult);
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(ResultCode.RESULT_OK);
                } else {
                    ResponseHandlerUtil.parseResponse(call, response, mResult, request);
                }
                if (request.getController() != null) {
                    request.getController().dismiss();
                }
            }
        };
        if (!isInThread) {
            mHandler = new WeakHandler(null, this);
        }
        call.enqueue(callback);
    }

    @Override
    public void handleMessages(Message msg) {
        try {
            switch (msg.what) {
                case ResultCode.RESULT_OK:
                    ResponseHandlerUtil.parseResponse(mCall, mResponse, mResult, mRequest);
                    break;

                case ResultCode.RESULT_FAIL:
                    ResponseHandlerUtil.parseFailure(mCall, null, mResult, mRequest, mException);
                    break;

                default:
                    break;
            }
        } catch (Throwable r) {
            r.printStackTrace();
        }

        reset();
    }

    public void reset() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

}
