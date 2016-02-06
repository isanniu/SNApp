package com.sannniu.ncore.webview;

import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;

/**
 * Created by niuzhikui on 2015/10/29.
 */
public class WapWebClient extends InjectedChromeClient {
    private ProgressBar mProgressBar;

    public WapWebClient(String injectedName, Class injectedCls) {
        super(injectedName, injectedCls);
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.mProgressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (mProgressBar != null) {
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE)
                    mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }
}
