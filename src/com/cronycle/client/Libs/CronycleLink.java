package com.cronycle.client.Libs;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.cronycle.client.CronycleApplication;
import com.cronycle.client.R;
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
	
	public void setFavouriteAsync(final Boolean value, final CronycleApplication app, final OnBooleanActionListener cb)
	{
		this.is_favourited = value;
		
		final CronycleLink instance = this;
		
		cb.onBefore();
		
		if (value == true) {
			API.getCronycleApiClient().favouriteLink(this.id, new Callback<Response>() {
				
				@Override
				public void success(Response arg0, Response arg1) {
					Toast.makeText(app.getApplicationContext(), R.string.added_to_favourites, Toast.LENGTH_SHORT).show();
					
					app.getCurrentCollections().getFavouriteCollection().links.add(0, instance);
					
					if (cb != null) cb.onComplete(true);
				}
				
				@Override
				public void failure(RetrofitError arg0) {
					is_favourited = !value;
					Toast.makeText(app.getApplicationContext(), R.string.api_errors_favourited, Toast.LENGTH_SHORT).show();
					if (cb != null) cb.onComplete(false);
				}
			});
		} else {
			API.getCronycleApiClient().unfavouriteLink(this.id, new Callback<Response>() {
				
				@Override
				public void success(Response arg0, Response arg1) {					
					for (CronycleLink l: app.getCurrentCollections().getFavouriteCollection().links) {
						if (l.id == instance.id) {
							app.getCurrentCollections().getFavouriteCollection().links.remove(l);
							break;
						}
					}
					
					Toast.makeText(app.getApplicationContext(), R.string.removed_from_favourites, Toast.LENGTH_SHORT).show();
					if (cb != null) cb.onComplete(true);
				}
				
				@Override
				public void failure(RetrofitError arg0) {
					is_favourited = !value;
					Toast.makeText(app.getApplicationContext(), R.string.api_errors_favourited, Toast.LENGTH_SHORT).show();
					if (cb != null) cb.onComplete(false);
				}
			});
		}
	}
}
