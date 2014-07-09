package com.cronycle.client;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Typeface;

import com.cronycle.client.Libs.CronycleCollection;

public class CronycleApplication extends Application {
	
	private CronycleCollection[] currentCollections;
	
	public Typeface proximaNovaBold;
	public Typeface proximaNovaRegular;
	public Typeface proximaNovaSemiBold;
	public Typeface proximaNovaLight;
	 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		
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
 
	public void setCollections(CronycleCollection[] collections) {
		this.currentCollections = collections;
	}
	
	public CronycleCollection[] getCurrentCollections() {
		return this.currentCollections;
	}
}