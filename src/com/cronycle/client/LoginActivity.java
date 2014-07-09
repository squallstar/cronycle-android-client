package com.cronycle.client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleUser;

public class LoginActivity extends Activity {
	
	ProgressBar loader;
	Button btnTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        
        btnTwitter = (Button) findViewById(R.id.btnSignTwitter);
        loader = (ProgressBar) findViewById(R.id.loader);
        
        final Intent auth = new Intent(this, TwitterActivity.class);
        btnTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	startActivity(auth);
            }
        });
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if (CronycleUser.CurrentUser(getApplicationContext()) != null) {
    		btnTwitter.setVisibility(View.GONE);
    		loader.setVisibility(View.VISIBLE);
    		
    		Toast.makeText(getApplicationContext(), String.format("Logged in as %s", CronycleUser.CurrentUser().getFull_name()), Toast.LENGTH_SHORT).show();

			final Intent collectionsActivity = new Intent(this, CollectionsActivity.class);
			collectionsActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    		
    		//final ProgressDialog dialog = ProgressDialog.show(
        	//		this, "Loading Collections", "Please wait while we download your collections...", true);
    		
    		Thread thread = new Thread(new Runnable() {
        		public void run() {
        			API.getCronycleApiClient().getUserCollections(true, 4, CronycleUser.CurrentUser().getAuthToken(), new Callback<CronycleCollection[]>() {
        				
        				@Override
        				public void success(CronycleCollection[] collections, Response arg1) {
        					Toast.makeText(getApplicationContext(), String.format("Got %d collections", collections.length), Toast.LENGTH_LONG).show();
        					//dialog.dismiss();
        					
        					CronycleApplication app = (CronycleApplication)getApplication();
        					app.setCollections(collections);
        					
        					LoginActivity.this.runOnUiThread(new Runnable() {
                        		public void run() {
                        			startActivity(collectionsActivity);
                        		}
                        	});
        				}
        				
        				@Override
        				public void failure(final RetrofitError err) {
        					//dialog.dismiss();
        					
        					LoginActivity.this.runOnUiThread(new Runnable() {

								public void run() {
									
                        			new AlertDialog.Builder(getApplicationContext())
                				    .setTitle("Error")
                				    .setMessage(err.getMessage())
                				    .setPositiveButton(android.R.string.ok, null)
                				    .setIcon(android.R.drawable.ic_dialog_alert)
                				    .show();
                        		}
                        	}); 
        				}
        			});
        		}
        	});
    		
    		thread.start();
    	}
    }
}
