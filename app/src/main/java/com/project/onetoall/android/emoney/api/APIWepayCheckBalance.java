package com.project.onetoall.android.emoney.api;

import com.loopj.android.http.*;
/**
 * Created by c.anupol on 7/27/17.
 */

public class APIWepayCheckBalance {

    private static final String BASE_URL = "https://www.wepay.in.th/client_api.php";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
