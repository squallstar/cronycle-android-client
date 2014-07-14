package com.cronycle.client;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.CronycleDirectoryCategory;
import com.cronycle.client.adapters.DirectoryCategoryAdapter;

public class DirectoryFragment extends Fragment {

	Activity activity;
	
	DirectoryCategoryAdapter adapter;
	
	private View mRoot;
	
	ViewPager mViewPager;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		activity = getActivity();
  
        mRoot = inflater.inflate(R.layout.fragment_directory, container, false);
        
        ArrayList<CronycleDirectoryCategory> categories = ((CronycleApplication)activity.getApplication()).getCurrentCategories();
		
		adapter = new DirectoryCategoryAdapter(getActivity().getSupportFragmentManager(), categories);
		ViewPager mViewPager = (ViewPager) mRoot.findViewById(R.id.pager);
        mViewPager.setAdapter(adapter);  
        
        if (categories.size() == 0) {
        	API.getCronycleApiClient().getDirectoryCategories(new Callback<CronycleDirectoryCategory[]>() {
    			
    			@Override
    			public void success(CronycleDirectoryCategory[] cats, Response arg1) {
    				((CronycleApplication)activity.getApplication()).setCategories(cats);
    				
    				adapter.notifyDataSetChanged();
    			}
    			
    			@Override
    			public void failure(RetrofitError arg0) {
    				// Nothing
    			}
    		});
        }
		
        return mRoot;
    }
}
