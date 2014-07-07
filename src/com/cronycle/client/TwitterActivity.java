package com.cronycle.client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import twitter4j.auth.AccessToken;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleRequestSignIn;
import com.cronycle.client.Libs.CronycleResponseSignIn;

@SuppressLint("SetJavaScriptEnabled")
public class TwitterActivity extends Activity {
	
	WebView cronycleWebClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_twitter);
        
        cronycleWebClient = (WebView) findViewById(R.id.webview);
        cronycleWebClient.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        
        final String cbUrl = API.CALLBACKURL;

        cronycleWebClient.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                if (url.startsWith(cbUrl)) {
                    Uri uri = Uri.parse(url);
                    completeVerify(uri);
                    return true;
                }
                return false;
            }
        });
        cronycleWebClient.getSettings().setAppCacheEnabled(false);
        cronycleWebClient.getSettings().setJavaScriptEnabled(true);
        cronycleWebClient.clearCache(true);
        cronycleWebClient.clearFormData();

//        cronycleWebClient.setWebChromeClient(new WebChromeClient() {
//            
//        });
     
        loginToTwitter();
	}
	
	private void loginToTwitter() {
    	final ProgressDialog dialog = ProgressDialog.show(
    			this, "Contacting Twitter", "Please wait...", true);
    	
    	Thread thread = new Thread(new Runnable() {
        	@Override
            public void run() {
                try {                	

                	final String uri = API.Current().getOAuthLoginUrl();
                    
                    if (uri != null) {
                    	
                    	TwitterActivity.this.runOnUiThread(new Runnable() {
                    		public void run() {
                    			cronycleWebClient.loadUrl(uri);
                    		}
                    	});                    	
                    }
                    
                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
	
	private void completeVerify(Uri uri) {
        if (uri != null) {
            final String verifier = uri.getQueryParameter("oauth_verifier");
        	
        	final ProgressDialog dialog = ProgressDialog.show(
        			this, "Logging into Twitter", "Please wait...", true);
        	
            try {
            	Thread thread = new Thread(new Runnable() {
            		public void run() {
            			AccessToken token = API.Current().getAccessToken(verifier);

            	        API.getCronycleApiClient().twitterSignIn(new CronycleRequestSignIn(token.getUserId(), token.getToken(), token.getTokenSecret()), new Callback<CronycleResponseSignIn>() {
            				
            				@Override
            				public void success(CronycleResponseSignIn arg0, Response arg1) {
            					// TODO Auto-generated method stub
            					
            				}
            				
            				@Override
            				public void failure(RetrofitError arg0) {
            					// TODO Auto-generated method stub
            					
            				}
            			});
            			
            			dialog.dismiss();

                        setResult(Activity.RESULT_OK, getIntent());
                        finish();            			
            		}
            	});
            	
            	thread.start();
            	
            } catch (Exception e) {
                Log.d("Error", "Cannot get AccessToken: " + e.getMessage());
                setResult(Activity.RESULT_OK, getIntent());
                finish();
            }
        }
    }
}
