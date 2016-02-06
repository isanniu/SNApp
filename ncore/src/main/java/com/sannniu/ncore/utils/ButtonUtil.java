package com.sannniu.ncore.utils;/**
 * Created by ry_zhaolei on 2015/1/27.
 */

/**
 * Description: 关于button的相关方法
 * Author: Lei.Zhao
 * Date: 2015-1-27  20:40:08CpuUtil
 ClassUtil
 */
public class ButtonUtil {

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            lastClickTime = time;
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
