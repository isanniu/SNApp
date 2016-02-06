package com.sannniu.ncore.http;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;



/**
 * Created by zhuchenxi on 15/10/11.
 */
public class HeaderInterceptors implements Interceptor {
    public static String TOKEN = "";
    public static String UID = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
                .addHeader("platform", "android")
                .addHeader("uid", UID)
                .addHeader("token", TOKEN)
                .build();
        return chain.proceed(newRequest);
    }
}
