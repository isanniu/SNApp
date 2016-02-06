package com.sannniu.ncore.http;

import com.sannniu.ncore.utils.LogUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

class LoggingInterceptor implements Interceptor {
    private static int INDEX;

    @Override public Response intercept(Chain chain) throws IOException {

      int curIndex = INDEX;
      Request request = chain.request();

      long t1 = System.nanoTime();
      LogUtils.i( String.format(curIndex + "请求 %s on %s%n%s",
              request.url(), chain.connection(), request.headers()));

      Response response = chain.proceed(request);

      long t2 = System.nanoTime();
        LogUtils.i(String.format(curIndex + "返回 for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

    return response;
    }
}