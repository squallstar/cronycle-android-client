package com.cronycle.client;

import com.cronycle.client.adapters.CollectionsAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class CollectionsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_collections);
	    
	    CronycleApplication app = (CronycleApplication) getApplication();
	    CollectionsAdapter adapter = new CollectionsAdapter(this, app.getCurrentCollections());
	
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(adapter);
	}

}
