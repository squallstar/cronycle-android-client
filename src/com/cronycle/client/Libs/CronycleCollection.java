package com.cronycle.client.Libs;

import android.graphics.Color;

public class CronycleCollection {
	
	public int id;
	public String private_id;
	public String name;
	public String description;
	public int position;
	public boolean owned_collection;
	public boolean publicly_visible;
	public String type;
	public int total_links_count;
	
	public CronycleCollectionSettings settings;
	
	public static class CronycleCollectionSettings {
		public String color;
		private int internalColor = 0;
		
		public CronycleCollectionLinksSettings links;
		
		public static class CronycleCollectionLinksSettings {
			public String displayStyle;
		}
		
		public int getColor() {
			
			if (internalColor == 0) {
				if (color.equals("blue")) internalColor = Color.parseColor("#566fc3");
				else if (color.equals("charcoal")) internalColor = Color.parseColor("#606468");
				else if (color.equals("cyan")) internalColor = Color.parseColor("#67c8c3");
				else if (color.equals("green")) internalColor = Color.parseColor("#32d890");
				else if (color.equals("orange")) internalColor = Color.parseColor("#fa6828");
				else if (color.equals("pink")) internalColor = Color.parseColor("#dc5661");
				else if (color.equals("red")) internalColor = Color.parseColor("#ed3f48");
				else if (color.equals("yellow")) internalColor = Color.parseColor("#ffb341");
				else internalColor = Color.parseColor("#ffb341");
			}
			
			return internalColor;
		}
	}
	
	public CronycleCollectionFilter[] filters;
	
	public CronycleAsset cover_asset;
	
	public CronycleLink[] links;
	
	public CronycleCollectionUser user;
	
	public static class CronycleCollectionUser {
		public int id;
		public String full_name;
		public String nickname;
		public String image_url;
	}
}
