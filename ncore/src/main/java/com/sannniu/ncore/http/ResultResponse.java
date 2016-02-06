package com.sannniu.ncore.http;

import com.google.gson.annotations.SerializedName;
import com.sannniu.ncore.app.AppConfig;

import java.io.Serializable;

/**
 * Created by niuzhikui on 2016/1/25.
 */
public class ResultResponse implements Serializable {
    @SerializedName(AppConfig.WRAPPER.STATUS)
    public int status;
    @SerializedName(AppConfig.WRAPPER.INFO)
    public String info;
}
