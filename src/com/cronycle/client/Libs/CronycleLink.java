package com.cronycle.client.Libs;

import android.text.format.DateUtils;

public class CronycleLink {
	public int id;
	public String url;
	public String name;
	public String description;
	public String content;
	
	public int published_at;
	
	public CronycleAsset lead_image;
	public Boolean lead_image_in_content;
	
	public CronycleCollection parentCollection;
	
	public CronycleSource[] sources;	
	
	public CharSequence getPostedAgo() {	    
	    return DateUtils.getRelativeTimeSpanString(published_at * 1000L);
	}
	
	public String getSourceFullName() {
		if (sources.length > 0) return sources[0].full_name;
		return "";
	}
	
	public String getGoodLookingTitle() {
		if (name != null) return name.replaceFirst("^Twitter / [A-z0-9]+: ", "");
		return "Untitled";
	}
}
