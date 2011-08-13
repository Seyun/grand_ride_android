package com.ppassa;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;


public class Grand_ride_androidActivity extends Activity {
	WebView mWebView;
	
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
        //mWebView.loadUrl("http://192.168.157.128:3000/rides/1/show_status");
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

    class PositionManager {
    	// for temporary purpose
    	int mCnt = 0;
    	double[] mLats = new double[] {40.307807, 40.312846676251944, 40.323088495646196, 40.331267746544405, 40.34235722744848, 40.350305202868135};
    	double[] mLons = new double[] {-74.6576801, -74.65995461324462, -74.64785248616943, -74.63798195699462, -74.65094239095458, -74.65699345449218};

    			
    	PositionManager() {
    	}
    	
    	Position getCurrentPosition() {
    		if (mCnt < mLats.length) {
    			double lat = mLats[mCnt];
    			double lon = mLons[mCnt];
    			mCnt += 1;
    			return new Position(lat, lon);
    		} else {
    			return null;
    		}
    	}
    }
    
    class UpdatePositionTask extends TimerTask {
    	PositionManager mPosManager;
    	
    	UpdatePositionTask() {
    		mPosManager = new PositionManager();
    	}
    	
    	public void run() {
    		Log.d("timer_task", "timertask run!");
    		Position pos = mPosManager.getCurrentPosition();
    		if (pos != null)
    			mWebView.loadUrl("javascript:recordTrack("+pos.latitude+", "+pos.longitude+")");
    		else
    			Log.d("update_task", "position is not available");
    		return;
    	}
    }
    
    private class MyWebViewClient extends WebViewClient {
    	private Timer mTimer;
    	
    	public MyWebViewClient() {
    		this.mTimer = new Timer("timer_update_pos", true);
    	}
    	
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	Log.i("webviewclient", "url: "+url);
        	if (url.startsWith("toapp://ensureTracking")) {
        		Log.d("webviewclient", "start tracking");
        		this.mTimer = new Timer("timer_update_pos", true);
        		UpdatePositionTask task = new UpdatePositionTask();
        		mTimer.schedule(task, 3000, 3000);
        		return true;
        	} else {
        		Log.d("webviewclient", "leaving the page, so stop tracking");
        		mTimer.cancel();
        		return false;
        	}
        }
    }
}

