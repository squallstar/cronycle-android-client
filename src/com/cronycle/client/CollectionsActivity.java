package com.cronycle.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cronycle.client.adapters.CollectionsAdapter;

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
	    
	    gridview.setOnItemClickListener(new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	            TextView songTitle = (TextView)view.findViewById(R.id.title);

	            Intent collectionIntent = new Intent(getBaseContext(), CollectionActivity.class);
                startActivity(collectionIntent);
	        }
	    });
	}

}
