package com.cronycle.client;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.API.OnFetchListener;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleLink;
import com.cronycle.client.adapters.LinksAdapter;

public class CollectionActivity extends Activity implements OnRefreshListener {
	
	CronycleCollection collection;
	
	SwipeRefreshLayout swipeLayout;
	LinksAdapter adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.activity_collection);
	    
	    overridePendingTransition(R.xml.push_left_in, R.xml.push_left_out);
	    
	    final CronycleApplication app = (CronycleApplication) getApplication();
	    
	    Intent thisIntent = getIntent();
	    String private_id = thisIntent.getStringExtra("private_id");
	    
	    for (CronycleCollection c: app.getCurrentCollections()) {
	    	if (c.private_id.equals(private_id)) {
	    		collection = c;
	    		break;
	    	}
	    }
	    
	    this.setTitle(collection.name);
	    getActionBar().setBackgroundDrawable(new ColorDrawable(collection.settings.getColor()));    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    adapter = new LinksAdapter(this, collection);
	
	    GridView itemsview = (GridView) findViewById(R.id.itemsview);
	    itemsview.setAdapter(adapter);
	    
	    swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
	    swipeLayout.setColorSchemeColors(collection.settings.getColor(), Color.parseColor("#ffb341"), collection.settings.getColor(), Color.parseColor("#2E3339"));
	    
	    itemsview.setOnItemClickListener(new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	        	CronycleLink link = adapter.getItem(position);
	        	
	        	String u = link.url.toLowerCase(Locale.ENGLISH);
	        	
	        	if (u.contains("facebook.com/") ||
	        		u.contains("twitter.com/") ||
	        		u.contains("instagram.com/") ||
	        		u.contains("youtube.com/") ||
	        		u.contains("ebay.com/") ||
	        		u.contains("spotify.com")
	        		
	        	) {
	        		// Native intent
		        	Intent i = new Intent(Intent.ACTION_VIEW);
		        	i.setData(Uri.parse(link.url));
		        	startActivity(i);
	        	} else {
	        		// Reader mode
	        		link.parentCollection = collection;
	        		Intent readerIntent = new Intent(getBaseContext(), ReaderActivity.class);
	        		
	        		app.nextActivitySubject = link;
	        		
	                startActivity(readerIntent);
	        	}
	        }
	    });
	    
	    if (collection.links.size() == 0) {
	    	swipeLayout.setRefreshing(true);
	    	
	    	API.Current().fetchMoreLinksForCollection(collection, new OnFetchListener() {
				
				@Override
				public void onComplete(Boolean success, int newLinksCount) {
					swipeLayout.setRefreshing(false);
					if (newLinksCount > 0) adapter.notifyDataSetChanged();					
				}
			});
	    }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.xml.push_right_out, R.xml.push_right_in);
	}
	
	@Override
	public void onRefresh() {
		API.Current().fetchNewLinksForCollection(collection, new OnFetchListener() {
			
			@Override
			public void onComplete(Boolean success, int newLinksCount) {
				swipeLayout.setRefreshing(false);
				
				if (newLinksCount > 0) {
					adapter.notifyDataSetChanged();	
					Toast.makeText(getApplicationContext(), String.format("%d new entries", newLinksCount), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Nothing new to read", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
