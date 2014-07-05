package com.cronycle.client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

import com.cronycle.client.Libs.API;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initControl();
        
        if (android.os.Build.VERSION.SDK_INT > 8) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
        }
        
        String uri = API.Current().getOAuthLoginUrl();
        
        if (uri != null) {
        	this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        }
        
//        Log.i("Login", "Getting user");
//        
//        API.getCronycleApiClient().getUser(1, new Callback<CronycleUserData>() {
//			
//			@Override
//			public void success(CronycleUserData user, Response arg1) {
//				// TODO Auto-generated method stub
//				Log.i("User API", user.getFull_name());
//				
//			}
//			
//			@Override
//			public void failure(RetrofitError arg0) {
//				// TODO Auto-generated method stub
//				Log.i("User API", "Error while getting the user: " + arg0.toString());
//			}
//		});
    }
    
    private void initControl() {
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(API.CALLBACKURL)) {

        }
    }
}
