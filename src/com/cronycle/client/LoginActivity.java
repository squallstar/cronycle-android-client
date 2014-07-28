package com.cronycle.client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleCollections;
import com.cronycle.client.Libs.CronycleRequestSignIn;
import com.cronycle.client.Libs.CronycleResponseSignIn;
import com.cronycle.client.Libs.CronycleUser;

@SuppressLint("InflateParams")

public class LoginActivity extends Activity {
	
	ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Crashlytics.start(this);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
        setContentView(R.layout.activity_login);
        
        Button btnTwitter = (Button) findViewById(R.id.btnSignTwitter);
        
        loader = (ProgressBar) findViewById(R.id.loader);
        
        final Intent auth = new Intent(this, TwitterActivity.class);
        btnTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	startActivity(auth);
            }
        });
        
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	
            	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            	final View loginView = inflater.inflate(R.layout.login_modal, null);
            	
            	new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Log in")
                .setMessage("Please enter your login details")
                .setView(loginView)
                .setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                         String email = ((EditText)loginView.findViewById(R.id.email_address)).getText().toString();
                         String password = ((EditText)loginView.findViewById(R.id.password)).getText().toString();
                         
                         if (email.length() > 0 && password.length() > 0) {
                        	 findViewById(R.id.login_buttons).setVisibility(View.GONE);
                        	 loader.setVisibility(View.VISIBLE);
                        	 
                        	 API.getCronycleApiClient().signIn(new CronycleRequestSignIn(email, password), new Callback<CronycleResponseSignIn>() {
                 				
                 				@Override
                 				public void success(CronycleResponseSignIn response, Response arg1) {
                 					
                 					CronycleUser.SetCurrentUser(response, getApplicationContext());
                 					onResume();
                 				}
                 				
                 				@Override
                 				public void failure(RetrofitError arg0) {

                               	    loader.setVisibility(View.GONE);
                 					findViewById(R.id.login_buttons).setVisibility(View.VISIBLE);
                 					
                 					new AlertDialog.Builder(LoginActivity.this)
                 	    		    .setTitle("Oops...")
                 	    		    .setMessage("The provided email or password didn't match. Please check your login details again.")
                 	    		    .setPositiveButton(android.R.string.ok, null)
                 	    		    .setIcon(android.R.drawable.ic_dialog_alert)    		    
                 	    		    .show();
                 					
                 				}
                 			});
                        	 
                         }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
            }
        });
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if (CronycleUser.CurrentUser(getApplicationContext()) != null) {
    		
    		findViewById(R.id.login_buttons).setVisibility(View.GONE);
    		
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
    	
			final Intent collectionsActivity = new Intent(this, MainActivity.class);
    		
			final Context ctx = this;
    		
    		Thread thread = new Thread(new Runnable() {
        		public void run() {
        			API.getCronycleApiClient().getUserCollections(false, 0, new Callback<CronycleCollections>() {
        				
        				@Override
        				public void success(CronycleCollections collections, Response arg1) {
        					
        					CronycleApplication app = (CronycleApplication)getApplication();
        					app.setCollections(collections);
        					
        					LoginActivity.this.runOnUiThread(new Runnable() {
                        		public void run() {
                        			collectionsActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
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
