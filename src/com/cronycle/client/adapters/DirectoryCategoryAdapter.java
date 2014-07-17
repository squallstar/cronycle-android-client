package com.cronycle.client.adapters;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cronycle.client.CategoryFragment;
import com.cronycle.client.Libs.CronycleDirectoryCategory;

public class DirectoryCategoryAdapter extends FragmentStatePagerAdapter {
	
	private ArrayList<CronycleDirectoryCategory> categories;
	
	public DirectoryCategoryAdapter(android.support.v4.app.FragmentManager fragmentManager, ArrayList<CronycleDirectoryCategory> categories) {
        super(fragmentManager);
        this.categories = categories;
    }
	
	@Override
    public Fragment getItem(int i) {
		Fragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        
        args.putString(CategoryFragment.ARG_SLUG, categories.get(i).slug);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).name;
    }

}
