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
    
    public class Position {
    	public double latitude = 0.0;
    	public double longitude = 0.0;
    	
    	public Position(double lat, double lon) {
    		this.latitude = lat;
    		this.longitude = lon;
    	}
    }
    
    public String get_test() {
    	return "Test";
    }
    
    public double getCurrentLatitude() {
    	return 100.;
    }
    
    public double getCurrentLongitude() {
    	return 120.;
    }
}
