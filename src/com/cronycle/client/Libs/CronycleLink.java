package com.cronycle.client.Libs;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.text.format.DateUtils;

import com.cronycle.client.Libs.API.OnBooleanActionListener;

public class CronycleLink {
	public int id;
	public String url;
	public String name;
	public String description;
	public String content;
	public Boolean is_favourited;
	
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
	
	public void setFavouriteAsync(Boolean value, final OnBooleanActionListener cb) {
		if (value == true) {
			API.getCronycleApiClient().favouriteLink(this.id, new Callback<Response>() {
				
				@Override
				public void success(Response arg0, Response arg1) {
					is_favourited = true;
					if (cb != null) cb.onComplete(true);
				}
				
				@Override
				public void failure(RetrofitError arg0) {
					if (cb != null) cb.onComplete(false);
				}
			});
		} else {
			API.getCronycleApiClient().unfavouriteLink(this.id, new Callback<Response>() {
				
				@Override
				public void success(Response arg0, Response arg1) {
					is_favourited = false;
					if (cb != null) cb.onComplete(true);
				}
				
				@Override
				public void failure(RetrofitError arg0) {
					if (cb != null) cb.onComplete(false);
				}
			});
		}
	}
}
