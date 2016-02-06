package com.sannniu.ncore.http;

import android.util.Log;

import com.google.gson.Gson;
import com.sannniu.ncore.app.AppConfig;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit.Converter;


/**
 * Created by niuzhikui on 2016/1/25.
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.d("Network", "response>>" + response);
        JSONObject jsonObject = null;
        int status = 0;

        try {
            jsonObject = new JSONObject(response);
            status = jsonObject.getInt(AppConfig.WRAPPER.STATUS);
        } catch (JSONException e) {
            throw new ServiceException(AppConfig.CODE.ANALYSIS_ERROR, "数据解析错误");
        }

        if (status == AppConfig.CODE.SUCCEED) {
            if (jsonObject.has(AppConfig.WRAPPER.DATA))
                try {
                    return gson.fromJson(jsonObject.getString(AppConfig.WRAPPER.DATA), type);
                } catch (JSONException e) {
                    throw new ServiceException(AppConfig.CODE.ANALYSIS_ERROR, "数据解析错误");
                }
            else
                return null;

        } else {

            String info = "";
            if (jsonObject.has(AppConfig.WRAPPER.INFO))
                try {
                    info = jsonObject.getString(AppConfig.WRAPPER.INFO);
                } catch (JSONException e) {
                }
            throw new ServiceException(status, info);
        }

    }
}
