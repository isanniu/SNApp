package com.sannniu.ncore.mvp.expansion.overlay;


import com.sannniu.ncore.mvp.expansion.BeamBaseActivity;

/**
 * Created by Mr.Jude on 2015/8/17.
 */
public abstract class ViewExpansionDelegateProvider {
    public abstract ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity);

    public static ViewExpansionDelegateProvider DEFAULT = new ViewExpansionDelegateProvider() {
        @Override
        public ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity) {
            return new DefaultViewExpansionDelegate(activity);
        }
    };

}
