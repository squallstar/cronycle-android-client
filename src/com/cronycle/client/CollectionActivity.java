package com.cronycle.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.API.OnFetchListener;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.adapters.LinksAdapter;

public class CollectionActivity extends Activity {
	
	CronycleCollection collection;

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
	    
	    
	    final LinksAdapter adapter = new LinksAdapter(this, collection);
	
	    GridView itemsview = (GridView) findViewById(R.id.itemsview);
	    itemsview.setAdapter(adapter);
	    
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
	    	API.Current().loadLinksForCollection(collection, new OnFetchListener() {
				
				@Override
				public void onComplete(Boolean success) {
					adapter.notifyDataSetChanged();					
				}
			});
	    }
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.xml.push_right_out, R.xml.push_right_in);
	}

}
