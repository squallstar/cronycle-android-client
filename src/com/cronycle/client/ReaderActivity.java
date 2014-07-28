package com.cronycle.client;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.cronycle.client.Libs.API.OnBooleanActionListener;
import com.cronycle.client.Libs.CronycleLink;
import com.cronycle.client.Libs.CronycleWebView;
import com.cronycle.client.Libs.ScaleToFitWidthHeightTransform;
import com.cronycle.client.Libs.CronycleWebView.OnScrollChangedCallback;
import com.squareup.picasso.Picasso;

@SuppressLint("SetJavaScriptEnabled")
public class ReaderActivity extends Activity {
	
	private CronycleLink link;
	
	private Menu menu;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    link = (CronycleLink) ((CronycleApplication)getApplication()).getNextActivityObject();
	    
	    if (link == null) {
	    	finish();
	    	return;
	    }
	    
	    if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
	    
	    setContentView(R.layout.activity_reader);
	    
	    overridePendingTransition(R.xml.push_top_in, R.xml.push_hide);
	    
	    if (link.parentCollection != null) {
	    	this.setTitle(link.parentCollection.name);
	    } else {
	    	this.setTitle(link.name);
	    }
	    
	    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	
	    InputStream is;
		try {
			is = getAssets().open("reader_template.html");
			int size = is.available();

		    byte[] buffer = new byte[size];
		    is.read(buffer);
		    is.close();

		    String html = new String(buffer);
		    html = html.replace("{title}", link.getGoodLookingTitle())
		    		   .replace("{content}", link.content)
		    		   .replace("{author}", link.getSourceFullName())
		    		   .replace("{accent}", link.parentCollection.settings.getHEXColor())
		    		   .replace("{posted_ago}", link.getPostedAgo());
		    
		    CronycleWebView webview = (CronycleWebView)this.findViewById(R.id.webview);
		    
		    webview.setBackgroundColor(0x00000000);
		    webview.getSettings().setJavaScriptEnabled(true);
		    
		    if (link.lead_image != null) {
		    	
		    	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	        	Display display = wm.getDefaultDisplay();
	        	Point s = new Point();
	        	display.getSize(s);
	        	
	        	int height = s.x * link.lead_image.height / link.lead_image.width;
		    	
		    	final ImageView leadImage = (ImageView) findViewById(R.id.leadImage);
		    	leadImage.setLayoutParams(new FrameLayout.LayoutParams(s.x, height));
	        	
	        	double ratio = link.lead_image.height * 100.0 / link.lead_image.width;
	        	
	        	if (ratio > 100.0) ratio = 100.0;
	        	
	        	html = html.replace("{lead-img-ratio}", String.format("%f", ratio))
	        			   .replace(link.lead_image.url_archived_medium, "");
		    	
		    	Picasso
			    	.with(this)
			    	.load(link.lead_image.url_archived_medium)
			    	.transform(new ScaleToFitWidthHeightTransform(s.x, false))
			    	.into(leadImage);
		    	
		    	webview.setOnScrollChangedCallback(new OnScrollChangedCallback() {
					
					@Override
					public void onScroll(int l, int t, int oldl, int oldt) {

						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(leadImage.getLayoutParams());
						params.setMargins(0, (int) -t/3, 0, 0);
						leadImage.setLayoutParams(params);
					}
				});
		    	
		    } else {
		    	html = html.replace("{top-margin}", "0");
		    }
		    
		    webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", "");
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			finish();
	    	return;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        case R.id.action_share:
	        	Intent share = new Intent(android.content.Intent.ACTION_SEND);
	            share.setType("text/plain");
	            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	         
	            // Add data to the intent, the receiving app will decide
	            // what to do with it.
	            share.putExtra(Intent.EXTRA_SUBJECT, link.name);
	            share.putExtra(Intent.EXTRA_TEXT, link.url);
	         
	            startActivity(Intent.createChooser(share, "Share article"));
	        	return true;
	        case R.id.action_favourite:
	        	toggleFavourite();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.xml.reader_menu, menu);
	    
	    this.menu = menu;
	    updateMenuTitles();
	    
	    return true;
	}
	
	private void updateMenuTitles() {
		if (menu == null) return;
		
        MenuItem favouriteMenu = menu.findItem(R.id.action_favourite);
        
        if (link.is_favourited) {
        	favouriteMenu.setTitle(R.string.remove_from_favourites);
        } else {
        	favouriteMenu.setTitle(R.string.add_to_favourites);
        }
	}
	
	private void toggleFavourite() {
		link.setFavouriteAsync(!link.is_favourited, (CronycleApplication) getApplication(), new OnBooleanActionListener() {
			
			@Override
			public void onComplete(Boolean success) {
				if (success) {
					updateMenuTitles();
				}
			}
			
			@Override
			public void onBefore() {
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.xml.push_right_out, R.xml.push_right_in);
	}
}
