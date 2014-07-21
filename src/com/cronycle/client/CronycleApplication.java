package com.cronycle.client;

import java.util.ArrayList;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Typeface;

import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleCollections;
import com.cronycle.client.Libs.CronycleDirectoryCategory;

public class CronycleApplication extends Application {
	
	private CronycleCollections currentCollections;
	private ArrayList<CronycleDirectoryCategory> currentCategories;
	
	public Typeface proximaNovaBold;
	public Typeface proximaNovaRegular;
	public Typeface proximaNovaSemiBold;
	public Typeface proximaNovaLight;
	
	public Object nextActivitySubject;
	 
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
	
	public Object getNextActivityObject()
	{
		if (nextActivitySubject == null) return null;
		
		Object x = nextActivitySubject;
		nextActivitySubject = null;
		return x;
	}
}