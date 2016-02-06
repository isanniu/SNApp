package com.sannniu.ncore.http;

import com.sannniu.ncore.app.AppConfig;
import com.sannniu.ncore.app.BaseApplication;
import com.sannniu.ncore.utils.LogUtils;
import com.sannniu.ncore.utils.ToastFactory;

import rx.Observer;

/**
 * Created by Mr.Jude on 2015/8/24.
 * 服务器返回的回调
 */
public class ServiceResponse<T> implements Observer<T> {


    @Override
    public void onNext(T t) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        if (e.getCause() instanceof ServiceException) {
            LogUtils.i("Server Error:" + e.getLocalizedMessage());
            onServiceError(((ServiceException) e.getCause()).getStatus(), ((ServiceException) e.getCause()).getInfo());
        } else {
            LogUtils.i("UnKnow Error:" + e.getLocalizedMessage());
            onServiceError(AppConfig.CODE.NET_INVALID, "网络错误");
        }
    }

    public void onServiceError(int status, String info) {
        if (status == AppConfig.CODE.LOGIN_INVALID) {
            jumpLogin();
        }
        ToastFactory.show(BaseApplication.getInstance(), info);
    }

    public void jumpLogin() {
    }

}
