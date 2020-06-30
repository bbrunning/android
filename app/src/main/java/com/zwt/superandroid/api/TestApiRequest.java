package com.zwt.superandroid.api;

import com.zwt.superandroid.util.api.APIBaseRequest;
import com.zwt.superandroid.util.api.BaseResponseData;

import java.util.List;


/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/11
 * Time: 19:17
 */
public class TestApiRequest extends APIBaseRequest<TestApiRequest.TestApiResponse> {
    @Override
    public String getUrl() {
        return "https://www.wanandroid.com/article/list/0/json";
    }

    public static class TestApiResponse extends BaseResponseData {
        private int curPage;
        private List<TestApiResponseItem> datas;
    }

    public static class TestApiResponseItem {
        private String apkLink;
        private int audit;
    }
}
