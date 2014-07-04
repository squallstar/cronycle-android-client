package com.cronycle.client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleUserData;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        String uri = API.Current().getOAuthLoginUrl();
        
        this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        
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
}
