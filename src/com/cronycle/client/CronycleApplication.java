package com.cronycle.client;

import java.util.List;

import android.app.Application;
import android.content.res.Configuration;

import com.cronycle.client.Libs.CronycleCollection;

public class CronycleApplication extends Application {
	
	private List<CronycleCollection> currentCollections;
	 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
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