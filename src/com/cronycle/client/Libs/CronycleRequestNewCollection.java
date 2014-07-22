package com.cronycle.client.Libs;


public class CronycleRequestNewCollection {
	
	private CronycleCollection collection;
	
	public CronycleRequestNewCollection(CronycleCollection c) {
		this.collection = c;
	}

	public CronycleCollection getCollection() {
		return collection;
	}
}
