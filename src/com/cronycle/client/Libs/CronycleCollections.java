package com.cronycle.client.Libs;

import java.util.ArrayList;

public class CronycleCollections extends ArrayList<CronycleCollection> {

	private static final long serialVersionUID = 6903924228062691051L;
	
	/**
	 * Get the favourite collection
	 * When it doesn't exist, a new one will be created and added to the array
	 * @return
	 */
	public CronycleCollection getFavouriteCollection() {
		for (CronycleCollection c: this) {
			if (c.isFavouriteCollection()) return c;
		}
		
		CronycleCollection collection = CronycleCollection.FavouriteCollection();
		add(collection.position, collection);
		return collection;
	}
	
	/**
	 * Replace the current collections in the list with the given collections
	 * Also regenerates the favourite collection
	 * @param cs
	 */
	public void replaceWith(CronycleCollections cs) {
		clear();
		addAll(cs);
		getFavouriteCollection();
	}

}
