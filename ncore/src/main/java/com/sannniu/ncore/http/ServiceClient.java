package com.sannniu.ncore.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * @param <T>
 */
public class ServiceClient<T> {
    private OkHttpClient okHttpClient;

    private static ServiceClient mServiceClient;

    public static ServiceClient getInstance() {
        if (mServiceClient == null) {
            mServiceClient = new ServiceClient();
        }
        return mServiceClient;
    }

    public void ServiceClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
            okHttpClient.interceptors().add(new HttpLoggingInterceptor());//打印日志
            okHttpClient.interceptors().add(new HeaderInterceptors());
        }
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public T createService(final Class<T> serviceClass, String endpoint) {
        return new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(serviceClass);
    }

    public Gson buildGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .create();
    }

}
