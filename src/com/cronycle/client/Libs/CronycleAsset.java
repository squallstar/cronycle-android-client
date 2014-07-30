package com.cronycle.client.Libs;

public class CronycleAsset {
	public int id;
	public String media_type;
	public int width;
	public int height;
	public String url_archived;
	public String url_archived_medium;
	public String url_archived_small;
	public String embed_code;
	
	public String getMediumOrDefaultAsset() {
		if (url_archived_medium != null) return url_archived_medium;
		if (url_archived_small != null) return url_archived_small;
		return url_archived;
	}
}
