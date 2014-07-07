package com.cronycle.client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleResponseSignIn;
import com.cronycle.client.Libs.CronycleUserData;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Button btnTwitter = (Button) findViewById(R.id.btnSignTwitter);
        
        final Intent auth = new Intent(this, TwitterActivity.class);
        btnTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	startActivity(auth);
            }
        });
        
        
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
