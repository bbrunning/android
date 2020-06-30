package com.zwt.superandroid.util.api;

import android.content.Context;

import com.zwt.superandroid.util.Utils;
import com.zwt.superandroid.util.api.retrofit.OkHttpRequestUtil;
import com.zwt.superandroid.util.api.retrofit.RetrofitBase;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/8
 * Time: 19:45
 */
public abstract class APIBaseRequest<T extends BaseResponseData> {

    @OmittedAnnotation
    protected String url;

    @OmittedAnnotation
    private APIBaseResponse<T> response;

    @OmittedAnnotation
    private APIBase.ResponseListener<T> rspListener;

    @OmittedAnnotation
    private APIBaseRequest subRequest;

    @OmittedAnnotation
    private APIBase.ResponseListener subRspListener;

    @OmittedAnnotation
    private boolean inThread = false;

    @OmittedAnnotation
    APIController controller;

    @RequestBodyForm
    private String body;

    private BaseRequestData appDevice;

    public APIController getController() {
        return controller;
    }

    public void setController(APIController controller) {
        this.controller = controller;
    }


    public APIBaseRequest() {
        this.appDevice = BaseRequestData.getInstance();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public APIBaseResponse<T> getResponse() {
        return response;
    }

    public void setResponse(APIBaseResponse<T> response) {
        this.response = response;
    }

    public APIBase.ResponseListener<T> getRspListener() {
        return rspListener;
    }

    public void setRspListener(APIBase.ResponseListener<T> rspListener) {
        this.rspListener = rspListener;
    }

    public APIBaseRequest getSubRequest() {
        return subRequest;
    }

    public void setSubRequest(APIBaseRequest subRequest) {
        this.subRequest = subRequest;
    }

    public APIBase.ResponseListener getSubRspListener() {
        return subRspListener;
    }

    public void setSubRspListener(APIBase.ResponseListener subRspListener) {
        this.subRspListener = subRspListener;
    }

    public boolean isInThread() {
        return inThread;
    }

    public void setInThread(boolean inThread) {
        this.inThread = inThread;
    }

    public String getBody() {
        return body;
    }

    public BaseRequestData getAppDevice() {
        return appDevice;
    }

    public void setAppDevice(BaseRequestData appDevice) {
        this.appDevice = appDevice;
    }

    public void requestPost(Context context, boolean isShowLoading, APIBase.ResponseListener<T> listener) {
        requestController(context, isShowLoading, true, listener);
    }

    public void requestGet(Context context, boolean isShowLoading, APIBase.ResponseListener<T> listener) {
        requestController(context, isShowLoading, false, listener);
    }

    public void requestController(Context context, boolean isShowLoading, boolean isPost, final APIBase.ResponseListener<T> listener) {
        if (isShowLoading) {
            controller = new APIController(context);
            controller.show();
        }
        request(isPost, listener);
    }

    private void request(boolean isPost, final APIBase.ResponseListener<T> listener) {
        OkHttpClient client = RetrofitBase.getClient();
        RequestBody requestBody = null;
        if (isPost) {
            //构建请求体
            List<NameValuePair> params = APIUtils.getRequestParams(this);
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (Utils.getCount(params) > 0) {
                for (NameValuePair pair : params) {
                    bodyBuilder.add(pair.getName(), pair.getValue());
                }
            }
            requestBody = bodyBuilder.build();
        }
        Request.Builder builder = new Request.Builder()
                .url(getUrl())
                .method(isPost ? RetrofitBase.METHOD_POST : RetrofitBase.METHOD_GET, requestBody)
                .tag(RetrofitBase.NO_DEFAULT_BODY);
        //如果添加header
        //builder.addHeader("Host", "host");
        setRspListener(listener);
        Request request = builder.build();
        okHttpCall(client, request);
    }

    private void okHttpCall(OkHttpClient client, final Request request) {
        final Call call = client.newCall(request);
        new OkHttpRequestUtil(inThread).request(call, this);
    }


    public void uploadFile(Context context, File file, final APIBase.ResponseListener<T> listener) {
        controller = new APIController(context);
        controller.show();
        //2.创建RequestBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        OkHttpClient client = RetrofitBase.getClient();
        Request.Builder builder = new Request.Builder()
                .url(getUrl())
                .post(requestBody);
        //如果添加header
        //builder.addHeader("Host", "host");
        setRspListener(listener);
        Request request = builder.build();
        okHttpCall(client, request);
    }


    public abstract String getUrl();

    public static <T extends BaseResponseData> void onSuccess(APIBaseRequest<T> request) {
        request.onSuccess();
    }

    public static <T extends BaseResponseData> void onFail(APIBaseRequest<T> request) {
        request.onFail();
    }

    protected void onSuccess() {
    }

    protected void onFail() {
    }
}
