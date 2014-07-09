package com.cronycle.client;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.API.OnFetchListener;
import com.cronycle.client.Libs.CronycleCollection;
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
	    
	    CronycleApplication app = (CronycleApplication) getApplication();
	    
	    Intent thisIntent = getIntent();
	    String private_id = thisIntent.getStringExtra("private_id");
	    
	    for (CronycleCollection c: app.getCurrentCollections()) {
	    	if (c.private_id.equals(private_id)) {
	    		collection = c;
	    		break;
	    	}
	    }
	    
	    //getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
	    getActionBar().setBackgroundDrawable(new ColorDrawable(collection.settings.getColor()));    
	    this.setTitle(collection.name);
	    
	    SpannableString s = new SpannableString(collection.name);
	    s.setSpan(new TypefaceSpan("ProximaNova-Bold.ttf"), 0, s.length(),
	            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	 
	    // Update the action bar title with the TypefaceSpan instance
	    ActionBar actionBar = getActionBar();
	    actionBar.setTitle(s);
	    
	    
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
	        	String url = adapter.getItem(position).url;
	        	Intent i = new Intent(Intent.ACTION_VIEW);
	        	i.setData(Uri.parse(url));
	        	startActivity(i);
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
