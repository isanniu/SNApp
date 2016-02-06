package com.sannniu.ncore.http;

import com.sannniu.ncore.utils.LogUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Mr.Jude on 2015/8/25.
 * 对服务器请求的Observer的修改
 */
public class DefaultTransform<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable.getLocalizedMessage());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
