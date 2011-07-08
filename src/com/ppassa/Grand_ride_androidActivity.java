package com.ppassa;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Grand_ride_androidActivity extends Activity {
	WebView mWebView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new AndroidBridge(), "HybridApp");
        mWebView.loadUrl("http://grandride.heroku.com");
        
        mWebView.setWebViewClient(new HelloWebViewClient());
        
    }
    
    private class AndroidBridge {
//    	public void setMessage(final String arg) { // must be final
//    		handler.post(new Runnable() {
//		    	public void run() {
//		    		Log.d("HybridApp", "setMessage("+arg+")");
//		    		mTextView.setText(mStringHead+arg);
//		    	}
//    		});
//    	}
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}