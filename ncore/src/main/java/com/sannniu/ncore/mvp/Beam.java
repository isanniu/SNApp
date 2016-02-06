package com.sannniu.ncore.mvp;

import android.app.Activity;
import android.content.Context;

import com.sannniu.ncore.mvp.bijection.ActivityLifeCycleDelegate;
import com.sannniu.ncore.mvp.bijection.ActivityLifeCycleDelegateProvider;
import com.sannniu.ncore.mvp.expansion.BeamBaseActivity;
import com.sannniu.ncore.mvp.expansion.overlay.ViewExpansionDelegate;
import com.sannniu.ncore.mvp.expansion.overlay.ViewExpansionDelegateProvider;
import com.sannniu.ncore.mvp.model.ModelManager;

/**
 * Created by Mr.Jude on 2015/7/26.
 */
public final class Beam {
    private static ActivityLifeCycleDelegateProvider mActivityLIfeCycleDelegateProvider;

    private static ViewExpansionDelegateProvider mViewExpansionDelegateProvider;


    public static ActivityLifeCycleDelegate createActivityLifeCycleDelegate(Activity activity) {
        if (mActivityLIfeCycleDelegateProvider!=null)
            return mActivityLIfeCycleDelegateProvider.createActivityLifeCycleDelegate(activity);
        else return null;
    }

    public static ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity){
        if (mViewExpansionDelegateProvider==null)
            return ViewExpansionDelegateProvider.DEFAULT.createViewExpansionDelegate(activity);
        else return mViewExpansionDelegateProvider.createViewExpansionDelegate(activity);
    }

    public static void setActivityLifeCycleDelegateProvider(ActivityLifeCycleDelegateProvider activityLifeCycleDelegateClass){
        mActivityLIfeCycleDelegateProvider = activityLifeCycleDelegateClass;
    }

    public static void setViewExpansionDelegateProvider(ViewExpansionDelegateProvider viewExpansionDelegateProvider) {
        mViewExpansionDelegateProvider = viewExpansionDelegateProvider;
    }
    public static void init(Context ctx){
        ModelManager.init(ctx);
    }

}
