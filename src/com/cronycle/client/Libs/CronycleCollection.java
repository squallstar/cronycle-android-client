package com.cronycle.client.Libs;

import java.util.ArrayList;
import java.util.Locale;

import android.graphics.Color;

public class CronycleCollection {
	
	public int id;
	public String private_id;
	public String name = "New collection";
	public String description = "";
	public int position;
	public boolean owned_collection = true;
	public boolean publicly_visible = false;
	public String type;
	public int total_links_count = 0;
	
	private static String FAVOURITE_COLLECTION_PRIVATE_ID = "favourite_collection";
	
	public CronycleCollectionSettings settings;
	
	public CronycleCollection() {
		this.links = new ArrayList<CronycleLink>();
		this.settings = new CronycleCollectionSettings();
	}
	
	public static CronycleCollection FavouriteCollection() {
		CronycleCollection f = new CronycleCollection();
		f.id = 0;
		f.private_id = FAVOURITE_COLLECTION_PRIVATE_ID;
		f.name = "Favourites";
		f.position = CronycleUser.CurrentUser().getFavourite_collection_position();
		
		return f;
	}
	
	public boolean isFavouriteCollection() {
		return private_id.equals(FAVOURITE_COLLECTION_PRIVATE_ID);
	}
	
	public static class CronycleCollectionSettings {
		public String color = "yellow";
		private int internalColor = 0;
		
		public CronycleCollectionSettings() {
			links = new CronycleCollectionLinksSettings();
		}
		
		public CronycleCollectionLinksSettings links;
		
		public static class CronycleCollectionLinksSettings {
			public String displayStyle = "headline+image";
			
			public Boolean shouldDisplayImages() {
				return ! displayStyle.equals("headline");
			}
			
			public Boolean shouldDisplayTitle() {
				return ! displayStyle.equals("image");
			}
		}
		
		public int getColor() {
			
			// Cache further calls
			if (internalColor == 0) {
				
				if (color != null) {
					if (color.equals("blue")) internalColor = Color.parseColor("#566fc3");
					else if (color.equals("charcoal")) internalColor = Color.parseColor("#606468");
					else if (color.equals("cyan")) internalColor = Color.parseColor("#67c8c3");
					else if (color.equals("green")) internalColor = Color.parseColor("#32d890");
					else if (color.equals("orange")) internalColor = Color.parseColor("#fa6828");
					else if (color.equals("pink")) internalColor = Color.parseColor("#dc5661");
					else if (color.equals("red")) internalColor = Color.parseColor("#ed3f48");
					else if (color.equals("yellow")) internalColor = Color.parseColor("#ffb341");
	
					// Partners
					else if (color.equals("bluffers")) internalColor = Color.parseColor("#38aeb6");
					else if (color.equals("the_browser")) internalColor = Color.parseColor("#49746b");
					else if (color.equals("which")) internalColor = Color.parseColor("#5faee1");
				}
				
				// Final fallback
				if (internalColor == 0) {
					internalColor = Color.parseColor("#ffb341");
				}
			}
			
			return internalColor;
		}
	}
	
	public CronycleCollectionFilter[] filters;
	
	public CronycleAsset cover_asset;
	
	public ArrayList<CronycleLink> links;
	
	public CronycleCollectionUser user;
	
	public static class CronycleCollectionUser {
		public int id;
		public String full_name;
		public String nickname;
		public String image_url;
		
		public String getFullName() {
			if (full_name != null) return full_name;
			if (nickname != null) return nickname;
			return "Unknown user";
		}
	}
	
	public String getTotalLinksCount() {
		if (total_links_count > 1000) return String.format(Locale.ENGLISH, "%dK", Math.round(total_links_count / 1000));
		return String.format(Locale.ENGLISH, "%d", total_links_count);
	}
}
