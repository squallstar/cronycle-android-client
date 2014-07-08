package com.cronycle.client;

import java.util.List;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Typeface;

import com.cronycle.client.Libs.CronycleCollection;

public class CronycleApplication extends Application {
	
	private List<CronycleCollection> currentCollections;
	
	public Typeface proximaNovaBold;
	 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		
		proximaNovaBold = Typeface.createFromAsset(getAssets(), "ProximaNova-Bold.ttf");
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
 
	public void setCollections(List<CronycleCollection> collections) {
		this.currentCollections = collections;
	}
	
	public List<CronycleCollection> getCurrentCollections() {
		return this.currentCollections;
	}
}