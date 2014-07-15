package com.cronycle.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cronycle.client.adapters.CollectionsAdapter;

public class CollectionsFragment extends Fragment {

	Activity activity;
	
	CollectionsAdapter adapter;
	
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
	    adapter = new CollectionsAdapter(activity, app.getCurrentCollections());
	
	    GridView gridview = (GridView) v.findViewById(R.id.gridview);
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
	}
	
	 @Override
	  public void onResume() {
	     super.onResume();
	     
	     // Updates the ui anytime the collections count have changed
	     if (previousCollectionCount > 0 && adapter != null && adapter.getCount() != previousCollectionCount) {
	    	 adapter.notifyDataSetChanged();
	     }
	  }
}
