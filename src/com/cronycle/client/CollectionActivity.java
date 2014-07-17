package com.cronycle.client;

import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
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
	
	private Menu menu;
	
	private boolean isFollowing;
	
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
	    
	    this.collection = (CronycleCollection) app.getNextActivityObject();
	    
	    if (this.collection == null || !(this.collection instanceof CronycleCollection)) {
            Toast.makeText(getApplicationContext(), "Couldn't view this collection", Toast.LENGTH_LONG).show();
            finish();
            return;
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
	        case R.id.action_follow:
	        	onFollowClicked();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
	    // Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.xml.collection_menu, menu);
	    this.menu = menu;
	    
	    updateMenuTitles();
	    
	    return true;
	}
	
	private void updateMenuTitles() {
		if (menu == null) return;
		
        MenuItem followMenu = menu.findItem(R.id.action_follow);
        
        if (collection.isFavouriteCollection()) {
        	followMenu.setVisible(false);
        	return;
        }
        
        if (collection.owned_collection) {
            followMenu.setTitle(R.string.delete_collection);
        } else {
        	
        	isFollowing = false;
        	CronycleApplication app = (CronycleApplication) getApplication();
        	
        	for (CronycleCollection c : app.getCurrentCollections()) {
				if (c.id == collection.id) {
					isFollowing = true;
					break;
				}
			}
        	
        	if (isFollowing) {
        		followMenu.setTitle(R.string.unfollow_collection);
        	} else {
        		followMenu.setTitle(R.string.follow_collection);
        	}
        }
    }
	
	private void onFollowClicked() {
		final CronycleApplication app = (CronycleApplication) getApplication();
		
		if (collection.owned_collection) {
			API.getCronycleApiClient().deleteCollection(collection.id, new Callback<Response>() {

				@Override
				public void failure(RetrofitError err) {
					Toast.makeText(getApplicationContext(), "Cannot delete the collection", Toast.LENGTH_LONG).show();
				}

				@Override
				public void success(Response result, Response response) {
					Toast.makeText(getApplicationContext(), String.format("%s has been deleted from your Cronycle", collection.name), Toast.LENGTH_LONG).show();
					app.getCurrentCollections().remove(collection);
					finish();
				}
			});
		} else {
			if (isFollowing) {
				API.getCronycleApiClient().unfollowCollection(collection.id, new Callback<Response>() {

					@Override
					public void failure(RetrofitError err) {
						Toast.makeText(getApplicationContext(), "Cannot unsave the collection", Toast.LENGTH_LONG).show();
					}

					@Override
					public void success(Response result, Response response) {
						Toast.makeText(getApplicationContext(), String.format("%s has been unsaved", collection.name), Toast.LENGTH_LONG).show();
						app.getCurrentCollections().remove(collection);
						updateMenuTitles();
					}
				});
			} else {
				API.getCronycleApiClient().followCollection(collection.id, new Callback<Response>() {

					@Override
					public void failure(RetrofitError err) {
						Toast.makeText(getApplicationContext(), "Cannot add the collection to your Cronycle", Toast.LENGTH_LONG).show();
					}

					@Override
					public void success(Response result, Response response) {
						Toast.makeText(getApplicationContext(), String.format("%s has been added to your Cronycle", collection.name), Toast.LENGTH_LONG).show();
						app.getCurrentCollections().add(0, collection);
						updateMenuTitles();
					}
				});
			}
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
