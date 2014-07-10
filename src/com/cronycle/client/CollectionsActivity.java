package com.cronycle.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.cronycle.client.adapters.CollectionsAdapter;

public class CollectionsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_collections);
	    
	    CronycleApplication app = (CronycleApplication) getApplication();
	    final CollectionsAdapter adapter = new CollectionsAdapter(this, app.getCurrentCollections());
	
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(adapter);
	    
	    gridview.setOnItemClickListener(new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	            Intent collectionIntent = new Intent(getBaseContext(), CollectionActivity.class);
	            collectionIntent.putExtra("private_id", adapter.getItem(position).private_id);
                startActivity(collectionIntent);
	        }
	    });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    //Used to put dark icons on light action bar

	    //Create the search view
	    final SearchView searchView = new SearchView(getActionBar().getThemedContext());
	    searchView.setQueryHint("Search");


	    menu.add(Menu.NONE,Menu.NONE,1,"Search")
	        .setIcon(R.drawable.ic_action_search)
	        .setActionView(searchView)
	        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	    
	    final Context ctx = getApplicationContext();

	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
	        @Override
	        public boolean onQueryTextChange(String newText) {
	            if (newText.length() > 0) {
	                // Search

	            } else {
	                // Do something when there's no input
	            }
	            return false;
	        }
	        @Override
	        public boolean onQueryTextSubmit(String query) { 

	            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
	            
	            setProgressBarVisibility(true);

	            Handler handler = new Handler(); 
	            handler.postDelayed(new Runnable() { 
	                 public void run() { 
	                     setProgressBarVisibility(false);
	                 } 
	            }, 2000);

	            return false; }
	    });

	    return true;
	}

}
