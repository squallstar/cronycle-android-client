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
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		activity = getActivity();
  
        View rootView = inflater.inflate(R.layout.fragment_collections, container, false);
        
        fillView(rootView);
          
        return rootView;
    }

	public void fillView(View v) {
		
	    CronycleApplication app = (CronycleApplication) activity.getApplication();
	    final CollectionsAdapter adapter = new CollectionsAdapter(activity, app.getCurrentCollections());
	
	    GridView gridview = (GridView) v.findViewById(R.id.gridview);
	    gridview.setAdapter(adapter);
	    
	    gridview.setOnItemClickListener(new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	            Intent collectionIntent = new Intent(activity.getApplicationContext(), CollectionActivity.class);
	            collectionIntent.putExtra("private_id", adapter.getItem(position).private_id);
                startActivity(collectionIntent);
	        }
	    });
	}
	
	
}
