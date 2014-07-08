package com.cronycle.client.Libs;

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
		
		public CronycleCollectionLinksSettings links;
		
		public static class CronycleCollectionLinksSettings {
			public String displayStyle;
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
