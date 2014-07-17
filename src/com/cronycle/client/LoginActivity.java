package com.cronycle.client;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleUser;

public class LoginActivity extends Activity {
	
	ProgressBar loader;
	Button btnTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Crashlytics.start(this);
        
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
        	
        	if (!isNetworkAvailable()) {
        		new AlertDialog.Builder(this)
    		    .setTitle("Internet connection required")
    		    .setMessage("Cronycle requires an internet collection to run. Please check your connectivity and try again.")
    		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    		        public void onClick(DialogInterface dialog, int which) {
    		        	android.os.Process.killProcess(android.os.Process.myPid());
    		        }
    		    })
    		    .setIcon(android.R.drawable.ic_dialog_alert)    		    
    		    .show();
        		return;
        	}
    		
    		Toast.makeText(getApplicationContext(), String.format("Logged in as %s", CronycleUser.CurrentUser().getFull_name()), Toast.LENGTH_SHORT).show();

			final Intent collectionsActivity = new Intent(this, MainActivity.class);
    		
			final Context ctx = this;
    		
    		Thread thread = new Thread(new Runnable() {
        		public void run() {
        			API.getCronycleApiClient().getUserCollections(true, 4, new Callback<ArrayList<CronycleCollection>>() {
        				
        				@Override
        				public void success(ArrayList<CronycleCollection> collections, Response arg1) {
        					
        					// Adds the favourite collection
        					collections.add(CronycleCollection.FavouriteCollection(CronycleUser.CurrentUser().getFavourite_collection_position()));
        					
        					CronycleApplication app = (CronycleApplication)getApplication();
        					app.setCollections(collections);
        					
        					LoginActivity.this.runOnUiThread(new Runnable() {
                        		public void run() {
                        			collectionsActivity.addFlags(/*Intent.FLAG_ACTIVITY_NEW_TASK | */Intent.FLAG_ACTIVITY_CLEAR_TASK); 
                        			startActivity(collectionsActivity);
                        		}
                        	});
        				}
        				
        				@Override
        				public void failure(final RetrofitError err) {
        					
        					LoginActivity.this.runOnUiThread(new Runnable() {

								public void run() {
									
                        			new AlertDialog.Builder(ctx)
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
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
