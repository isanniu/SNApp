package com.sanniu.snapp.app;

import com.sanniu.snapp.R;
import com.sannniu.ncore.app.AppConfig;
import com.sannniu.ncore.app.BaseApplication;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by niuzhikui on 2016/1/23.
 */
public class App extends BaseApplication {
    @Override
    public void onCreate() {
        AppConfig.getInstance().log(true,"App").update(null, R.mipmap.ic_launcher);
        //设置适配
        AutoLayoutConifg.getInstance().useDeviceSize();
        super.onCreate();

    }
}
