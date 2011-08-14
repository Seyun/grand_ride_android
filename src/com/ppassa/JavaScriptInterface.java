package com.ppassa;

import android.webkit.WebView;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public class JavaScriptInterface {
	WebView webView;

    JavaScriptInterface(WebView webView) {
        this.webView = webView;
    }
    
    public void ensureTracking(int ride_id) {
    	webView.loadUrl("javascript:recordTrack(1, 1)");
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
    		if (pos != null) {
    			webView.loadUrl("javascript:record_track("+pos.latitude+", "+pos.longitude+")");
    			webView.loadUrl("javascript:update_map()");
    		} else
    			Log.d("update_task", "position is not available");
    		return;
    	}
    }
    
    public void startTracking() {
    	Log.d("startTracking", "start!");
    	new Timer("timer_update_pos", true);    	
    }
    
    public String get_test() {
    	return "Test";
    }
}
