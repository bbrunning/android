package com.zwt.superandroid.api;

import com.zwt.superandroid.util.api.APIBaseRequest;
import com.zwt.superandroid.util.api.BaseResponseData;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/15
 * Time: 12:10
 */
public class TestApiPostData extends APIBaseRequest<TestApiPostData.TestApiPostDataResponse> {

    private String name;

    public TestApiPostData(String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return "http://192.168.211.70:8080/webapp_war_exploded/getPerson";
    }

    public static class TestApiPostDataResponse extends BaseResponseData {

    }
}
