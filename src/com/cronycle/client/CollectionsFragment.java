package com.cronycle.client;

import java.util.ArrayList;

import org.askerov.dynamicgrid.DynamicGridView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.adapters.CollectionsAdapter;

public class CollectionsFragment extends Fragment implements OnRefreshListener {

	Activity activity;
	
	DynamicGridView gridview;
	
	CollectionsAdapter adapter;
	
	SwipeRefreshLayout swipeLayout;
	
	int previousCollectionCount = 0;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		activity = getActivity();
  
        View rootView = inflater.inflate(R.layout.fragment_collections, container, false);
        
        fillView(rootView);
          
        return rootView;
    }

	public void fillView(View v) {
		
	    final CronycleApplication app = (CronycleApplication) activity.getApplication();
	    adapter = new CollectionsAdapter(activity, app.getCurrentCollections(), 2);
	
	    gridview = (DynamicGridView) v.findViewById(R.id.gridview);
	    gridview.setAdapter(adapter);
	    
	    previousCollectionCount = adapter.getCount();
	    
	    gridview.setOnItemClickListener(new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	        	app.nextActivitySubject = adapter.getItem(position);
	        	
	            Intent collectionIntent = new Intent(activity.getApplicationContext(), CollectionActivity.class);
                startActivity(collectionIntent);
	        }
	    });
	    
	    gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	gridview.startEditMode(position);
                return false;
            }
        });
	    
	    swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
	    swipeLayout.setColorSchemeColors(Color.parseColor("#566FC3"), Color.parseColor("#ED3F48"), Color.parseColor("#31D890"), Color.parseColor("#606468"));
	   
	}
	
	 @Override
	  public void onResume() {
	     super.onResume();
	     
	     // Updates the ui anytime the collections count have changed
	     if (previousCollectionCount > 0 && adapter != null && adapter.getCount() != previousCollectionCount) {
	    	 adapter.notifyDataSetChanged();
	     }
	  }

	@Override
	public void onRefresh() {
		final CronycleApplication app = (CronycleApplication) activity.getApplication();
		
		API.getCronycleApiClient().getUserCollections(true, 4, new Callback<ArrayList<CronycleCollection>>() {
			
			@Override
			public void success(ArrayList<CronycleCollection> collections, Response arg1) {
				
				// Adds the favourite collection
				collections.add(CronycleCollection.FavouriteCollection());
				
				app.setCollections(collections);
				
				swipeLayout.setRefreshing(false);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void failure(final RetrofitError err) {
				swipeLayout.setRefreshing(false);
			}
		});
	}
}
