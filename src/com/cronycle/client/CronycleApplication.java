package com.cronycle.client;

import java.util.ArrayList;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Typeface;

import com.cronycle.client.Libs.CronycleCollections;
import com.cronycle.client.Libs.CronycleDirectoryCategory;

public class CronycleApplication extends Application {
	
	private CronycleCollections currentCollections;
	private ArrayList<CronycleDirectoryCategory> currentCategories;
	
	public Typeface proximaNovaBold;
	public Typeface proximaNovaRegular;
	public Typeface proximaNovaSemiBold;
	public Typeface proximaNovaLight;
	
	public Object subject_level_one;
	public Object subject_level_two;
	 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.currentCategories = new ArrayList<CronycleDirectoryCategory>();
		this.currentCollections = new CronycleCollections();
		
		proximaNovaBold = Typeface.createFromAsset(getAssets(), "ProximaNova-Bold.ttf");
		proximaNovaRegular = Typeface.createFromAsset(getAssets(), "ProximaNova-Regular.ttf");
		proximaNovaSemiBold = Typeface.createFromAsset(getAssets(), "ProximaNova-Semibold.ttf");
		proximaNovaLight = Typeface.createFromAsset(getAssets(), "ProximaNova-Light.ttf");
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
 
	public void setCollections(CronycleCollections collections) {
		this.currentCollections.replaceWith(collections);
	}
	
	public CronycleCollections getCurrentCollections() {
		return this.currentCollections;
	}
	
	public void setCategories(CronycleDirectoryCategory[] categories) {
		for(int x = 0; x < categories.length; x = x+1) {
			this.currentCategories.add(categories[x]);
		}
	}
	
	public ArrayList<CronycleDirectoryCategory> getCurrentCategories() {
		return this.currentCategories;
	}
	
	public void setSubject(Object obj, int level) {
		if (level == 1) subject_level_one = obj;
		else subject_level_two = obj;
	}
	
	public void setSubject(Object obj) {
		setSubject(obj, 1);
	}
	
	public Object getSubject(int level) {
		return level == 1 ? subject_level_one : subject_level_two;
	}
}