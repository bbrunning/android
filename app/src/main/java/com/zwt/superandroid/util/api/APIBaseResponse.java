package com.zwt.superandroid.util.api;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/8
 * Time: 19:49
 */
public class APIBaseResponse<T extends BaseResponseData> {
    private T data;
    private int errorCode;
    private String errorMsg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
