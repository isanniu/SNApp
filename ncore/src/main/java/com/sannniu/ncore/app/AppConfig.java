package com.sannniu.ncore.app;

import com.sannniu.ncore.utils.LogUtils;
import com.sannniu.ncore.utils.TextUtil;

/**
 * Created by niuzhikui on 2016/1/23.
 */
public class AppConfig {
    //调试模式
    public static  boolean DEBUG = true;

    private static AppConfig instance;
    public static  String sDownloadSDPath="/down";
    public static int sUpdateIcon;

    public AppConfig() {

    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    public AppConfig log(boolean debug, String tag) {
        DEBUG = debug;
        LogUtils.setDebug(DEBUG, tag);
        return this;
    }

    public AppConfig update(String downloadSDPath, int updateIcon) {
        if(!TextUtil.isEmpty(downloadSDPath)){
            sDownloadSDPath = downloadSDPath;
        }
        sUpdateIcon = updateIcon;
        return this;
    }

    public static final String KEY_DOWNLOAD_URL = "download_url";
    /**
     * 系统图片
     */
    public static final String APPIMAGE =  "img/";
    /**
     * 录音文件保存路径
     */
    public static final String APPRECORD =   "record/";


    public static final String APP_CRASH_BASE_PATH ="crash/";
    /**
     * 服务器地址
     */
    public static class API {
        public static final String BASE_URL_DEBUG = "";
        public static final String BASE_URL = "";
    }

    /**
     * Json包装
     */
    public static class WRAPPER {
        public static final String STATUS = "status";
        public static final String INFO = "info";
        public static final String DATA = "data";
    }

    /**
     * 状态码
     */
    public static class CODE {
        /**
         * 成功的
         */
        public static final int SUCCEED = 0;
        public static final int ANALYSIS_ERROR = -1;
        public static final int NET_INVALID = -2;
        public static final int SMS_ERROR = 1;
        public static final int RONG_ERROR = 2;
        public static final int QINIU_ERROR = 3;
        public static final int PARAMS_ERROR = 9;
        public static final int PARAMS_INVALID = 10;
        public static final int SERVER_ERROR = 100;
        public static final int LOGIN_INVALID = 400;
        public static final int PERMISSION_DENIED = 401;
        public static final int USER_INVALID = 1001;
        /**
         * 调用拍照request code
         */
        public static final int ACTION_CAMERA = 0x01;
        /**
         * 调用相册request code
         */
        public static final int ACTION_ALBUM = 0x02;
        /**
         * 打开扫码request code
         */
        public static final int ACTION_BARCODE = 0x03;

        /**
         * 打开录音request code
         */
        public static final int ACTION_RECORDER = 0x04;

        /**
         * 打开通讯录request code
         */
        public static final int ACTION_ADDRESSLIST = 0x05;
    }

}
