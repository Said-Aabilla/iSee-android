package com.example.iSee.Views;

import android.webkit.JavascriptInterface;

public class WebJavascriptInterface {
    ICallVIew  callActivity;

    /** Instantiate the interface and set the context */
    public WebJavascriptInterface( ICallVIew  callActivity) {
        this.callActivity = callActivity ;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void onPeerConnected() {
        this.callActivity.onPeerConnected();
    }
}
