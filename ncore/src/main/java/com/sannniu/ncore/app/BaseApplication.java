package com.sannniu.ncore.app;

import android.app.Activity;
import android.app.Application;

import com.sannniu.ncore.base.BaseActivity;
import com.sannniu.ncore.crash.CrashHandler;
import com.sannniu.ncore.image.GImage;
import com.sannniu.ncore.netstate.NetChangeObserver;
import com.sannniu.ncore.netstate.NetWorkUtil;
import com.sannniu.ncore.netstate.NetworkStateReceiver;
import com.sannniu.ncore.utils.AppManager;

/**
 * Created by niuzhikui on 2015/10/2.
 */
public class BaseApplication extends Application {
    public static BaseApplication sInstance;
    public Activity mCurrentActivity;
    private NetChangeObserver mNetChangeObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        init();
    }

    /**
     * 初始化一些数据
     */
    private void init() {
        initImageLoader();//加载图片
        registerNetWorkStateListener();// 注册网络状态监测器
        CrashHandler.getInstance().init(this);//异常捕获
//        if (AppConfig.DEBUG) {
//            LeakCanary.install(this);
//        }

    }

    /**
     * @return the application singleton instance
     */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    /**
     * 注册网络状态监听
     */
    private void registerNetWorkStateListener() {
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onConnect(NetWorkUtil.netType type) {
                super.onConnect(type);
                try {
                    BaseApplication.this.onConnect(type);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                try {
                    BaseApplication.this.onDisConnect();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };
        NetworkStateReceiver.registerObserver(mNetChangeObserver);
    }

    /**
     * 初始化imageloader
     */
    private void initImageLoader() {
        GImage.initImageLoader(this);
    }

    /**
     * 当前没有网络连接通知
     */
    public void onDisConnect() {
        mCurrentActivity = AppManager.getAppManager().currentActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof BaseActivity) {
                ((BaseActivity) mCurrentActivity).onDisConnect();
            }
        }
    }

    /**
     * 网络连接连接时通知
     */
    protected void onConnect(NetWorkUtil.netType type) {
        mCurrentActivity = AppManager.getAppManager().currentActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof BaseActivity) {
                ((BaseActivity) mCurrentActivity).onConnect(type);
            }
        }
    }

}
