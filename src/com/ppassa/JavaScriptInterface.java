package com.ppassa;

import android.webkit.WebView;


public class JavaScriptInterface {
	WebView webView;

    JavaScriptInterface(WebView webView) {
        this.webView = webView;
    }
    
    public void ensureTracking(int ride_id) {
    	webView.loadUrl("javascript:recordTrack(1, 1)");
    }
}
