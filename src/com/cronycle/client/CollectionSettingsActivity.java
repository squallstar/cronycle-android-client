package com.cronycle.client;

import com.cronycle.client.Libs.CronycleCollection;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class CollectionSettingsActivity extends Activity {

	private CronycleCollection collection;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.activity_collection_settings);
	    
	    overridePendingTransition(R.xml.push_left_in, R.xml.push_left_out);
	    
	    final CronycleApplication app = (CronycleApplication) getApplication();
	    
	    collection = (CronycleCollection) app.getSubject(1);
	    
	    this.setTitle("Display settings");
	    getActionBar().setBackgroundDrawable(new ColorDrawable(collection.settings.getColor()));    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.xml.push_right_out, R.xml.push_right_in);
	}

}
