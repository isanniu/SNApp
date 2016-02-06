package com.sanniu.snapp.model.service;

/**
 * Created by niuzhikui on 2016/1/28.
 */
public class NetClientFactory {

    public static NetClientFactory netClientFactory;

    public AppService mAppService;

    public static  NetClientFactory getInstance(){
        if(netClientFactory==null){
            netClientFactory=new NetClientFactory();
        }
        return netClientFactory;
    }
    public AppService getApiClient(){
        if(mAppService==null){

        }
        return mAppService;
    }
}
