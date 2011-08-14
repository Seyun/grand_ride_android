package com.ppassa;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
//import java.util.Timer;
//import java.util.TimerTask;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class Grand_ride_androidActivity extends Activity {
	WebView mWebView;
	MyLocationListener mlocListener;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        
        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) 
            {
                return false;
            }
            });
        
        mWebView.addJavascriptInterface(new JavaScriptInterface(mWebView), "Android");
        mWebView.loadUrl(Config.url);
        
        /* Use the LocationManager class to obtain GPS locations */
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    
    class Position {
    	double latitude;
    	double longitude; 
    	
    	
    	Position(double lat, double lon) {
    		this.latitude = lat;
    		this.longitude = lon;
    	}
    	
    	Position() {
    		this(0, 0);
    	}
    }
    
    private class MyLocationListener implements LocationListener
    {
    	private Position mPos;
    	
    	// for debug
    	private boolean DEBUG = true;
    	private int mCnt = 0;
    	private double[] mLats = new double[] {40.307807, 40.312846676251944, 40.323088495646196, 40.331267746544405, 40.34235722744848, 40.350305202868135};
    	private double[] mLons = new double[] {-74.6576801, -74.65995461324462, -74.64785248616943, -74.63798195699462, -74.65094239095458, -74.65699345449218};

    	
		public void onLocationChanged(Location loc) {
			Log.i("loc_manager", "onLocationChanged: "+loc);
			double lat = loc.getLatitude();
			double lon = loc.getLongitude();
			mPos = new Position(lat, lon);
		}
		
		public Position getCurrentPosition() {
			if (DEBUG) {
				if (mCnt < mLats.length) {
	    			double lat = mLats[mCnt];
	    			double lon = mLons[mCnt];
	    			mCnt += 1;
	    			return new Position(lat, lon);
	    		} else {
	    			return null;
	    		}
			} else {
				return mPos;
			}
		}
		
	    public void onProviderDisabled(String provider) {
	    	Log.i("loc_manager", "onProviderDisabled: "+provider);
	    }
	    
	    
	    public void onProviderEnabled(String provider) {
	    	Log.i("loc_manager", "onProviderEnabled: "+provider);
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    	Log.i("loc_manager", "onStatusChanged: "+status);
	    }

    }/* End of Class MyLocationListener */
    
    
    private class MyWebViewClient extends WebViewClient {
    	
    	public MyWebViewClient() {
    	}
    	
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	Log.i("webviewclient", "url: "+url);
        	if (url.startsWith("toapp://ensureTracking")) {
        		Position pos = mlocListener.getCurrentPosition();
        		if (pos != null)
        			mWebView.loadUrl("javascript:record_track("+pos.latitude+", "+pos.longitude+")");
        		mWebView.loadUrl("javascript:reload_page()");
        		return true;
        	} 
        	/*
        	else if (url.endsWith("show_status")) {
        		Position pos = this.mPosManager.getCurrentPosition();
        		mWebView.loadUrl("javascript:record_track("+pos.latitude+", "+pos.longitude+")");
        		//mWebView.loadUrl("javascript:update_map()");
        		mWebView.loadUrl("javascript:location.reload(true)");
        		return false;
        	} 
        	*/
        	else {
        		Log.d("webviewclient", "leaving the page, so stop tracking");
        		//mTimer.cancel();
        		return false;
        	}
        }
    }
}

