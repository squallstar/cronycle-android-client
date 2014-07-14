package com.cronycle.client;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleDirectoryCategory;
import com.cronycle.client.adapters.CollectionsAdapter;
import com.squareup.okhttp.Request;

public class CategoryFragment extends Fragment {

	public static final String ARG_SLUG = "slug";
	
	private CronycleDirectoryCategory category;

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
    	
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
        
        final CollectionsAdapter adapter = new CollectionsAdapter(getActivity(), category.collections);
    	
	    GridView gridview = (GridView) rootView.findViewById(R.id.collectionsGridView);
	    gridview.setAdapter(adapter);
        
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
