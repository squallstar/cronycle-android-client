package com.cronycle.client;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleDirectoryCategory;
import com.cronycle.client.adapters.PublicCollectionsAdapter;

public class CategoryFragment extends Fragment {

	public static final String ARG_SLUG = "slug";
	
	private CronycleDirectoryCategory category;

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_directory_category, container, false);
        
        Bundle args = getArguments();
        String slug = args.getString(ARG_SLUG);
        
        final CronycleApplication app = (CronycleApplication) getActivity().getApplication();
        
        for (CronycleDirectoryCategory cat: app.getCurrentCategories()) {
        	if (cat.slug.equals(slug)) {
        		category = cat;
        		break;
        	}
        }
        
        final PublicCollectionsAdapter adapter = new PublicCollectionsAdapter(getActivity(), category.collections);
    	
	    GridView gridview = (GridView) rootView.findViewById(R.id.collectionsGridView);
	    gridview.setAdapter(adapter);
	    
	    gridview.setOnItemClickListener(new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	        	app.setSubject(adapter.getItem(position));
	        	
	            Intent collectionIntent = new Intent(getActivity().getApplicationContext(), CollectionActivity.class);
                startActivity(collectionIntent);
	        }
	    });
        
        if (category.collections.size() == 0) {
        	
        	API.getCronycleApiClient().getCategoryCollections(category.slug, 20, new Callback<CronycleCollection[]>() {
				
        		@Override
    			public void success(CronycleCollection[] collections, Response arg1) {
        			for(int x = 0; x < collections.length; x = x+1) {
        				category.collections.add(collections[x]);
        			}
    				
    				adapter.notifyDataSetChanged();
    			}
    			
    			@Override
    			public void failure(RetrofitError arg0) {
    				// Nothing
    			}
			});
        }
        
        return rootView;
    }
	
}
