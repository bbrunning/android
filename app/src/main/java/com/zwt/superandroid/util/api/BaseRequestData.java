package com.zwt.superandroid.util.api;

/**
 * http Header公共参数
 */
public class BaseRequestData {

    private BaseRequestData() {
    }

    public static BaseRequestData getInstance() {
        return BaseRequestDataHolder.sInstance;
    }

    public static class BaseRequestDataHolder {
        static BaseRequestData sInstance = new BaseRequestData();
    }


}
