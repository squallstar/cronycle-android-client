package com.cronycle.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.cronycle.client.Libs.CronycleCollection;

public class CollectionActivity extends Activity {
	
	CronycleCollection collection;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.activity_collection);
	    
	    overridePendingTransition(R.xml.push_left_in, R.xml.push_left_out);
	    
	    Intent thisIntent = getIntent();
	    String private_id = thisIntent.getStringExtra("private_id");
	    
	    for (CronycleCollection c: ((CronycleApplication)getApplication()).getCurrentCollections()) {
	    	if (c.private_id.equals(private_id)) {
	    		collection = c;
	    		break;
	    	}
	    }
	    
	    getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
	    getActionBar().setBackgroundDrawable(new ColorDrawable(collection.settings.getColor()));    
	    this.setTitle(collection.name);
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.xml.push_right_out, R.xml.push_right_in);
	}

}
