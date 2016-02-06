package com.sannniu.ncore.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sannniu.ncore.netstate.NetWorkUtil;
import com.sannniu.ncore.netstate.NetworkStateReceiver;
import com.sannniu.ncore.utils.AppManager;

/**
 * Created by niuzhikui on 2015/10/2.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG=BaseActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        NetworkStateReceiver.registerNetworkStateReceiver(this);
        this.setContentView();
        this.initParams();
        this.initView();
        this.initListener();
        initData();
    }

    protected abstract void  setContentView();
    protected abstract void initParams();
    protected abstract void initView();
    protected abstract void initListener();
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    public void onConnect(NetWorkUtil.netType type) {
    }

    public void onDisConnect() {
    }
}
