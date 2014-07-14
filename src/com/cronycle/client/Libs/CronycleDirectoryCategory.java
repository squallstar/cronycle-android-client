package com.cronycle.client.Libs;

import java.util.ArrayList;

public class CronycleDirectoryCategory {
	
	public int id;
	public String name;
	public String slug;
	public int collection_count;
	
	public ArrayList<CronycleCollection> collections;
	
	public CronycleDirectoryCategory() {
		this.collections = new ArrayList<CronycleCollection>();
	}
}
