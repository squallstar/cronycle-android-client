package com.cronycle.client.Libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class CronycleUser {
	
	private static CronycleUser currentUserInstance;
	
	private static String PREFERENCES_KEY = "current_user";

    @Expose
    private int id;
    @Expose
    private String full_name;
    @Expose
    private String image_url;
    @Expose
    private String nickname;
    @Expose
    private String email;
    @Expose
    private boolean is_pro;
    @Expose
    private boolean has_password;
    @Expose
    private boolean needs_to_subscribe;
    @Expose
    private int favourite_collection_position;
    @Expose
    private int total_collections_count;
    @Expose
    private int total_links_count;
    @Expose
    private String auth_token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIs_pro() {
        return is_pro;
    }

    public void setIs_pro(boolean is_pro) {
        this.is_pro = is_pro;
    }

    public boolean isHas_password() {
        return has_password;
    }

    public void setHas_password(boolean has_password) {
        this.has_password = has_password;
    }

    public boolean isNeeds_to_subscribe() {
        return needs_to_subscribe;
    }

    public void setNeeds_to_subscribe(boolean needs_to_subscribe) {
        this.needs_to_subscribe = needs_to_subscribe;
    }

    public int getFavourite_collection_position() {
        return favourite_collection_position;
    }

    public void setFavourite_collection_position(int favourite_collection_position) {
        this.favourite_collection_position = favourite_collection_position;
    }

    public int getTotal_collections_count() {
        return total_collections_count;
    }

    public void setTotal_collections_count(int total_collections_count) {
        this.total_collections_count = total_collections_count;
    }

    public int getTotal_links_count() {
        return total_links_count;
    }

    public void setTotal_links_count(int total_links_count) {
        this.total_links_count = total_links_count;
    }
    
    public void setAuthToken(String token) {
    	this.auth_token = token;
    }
    
    public String getAuthToken() {
    	return auth_token;
    }
    
    public void SaveToPreferences(Context ctx)
    {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    	
    	Editor prefsEditor = prefs.edit();
    	Gson gson = new Gson();
    	String json = gson.toJson(this);
    	prefsEditor.putString(PREFERENCES_KEY, json);
        prefsEditor.commit();
    }
    
    public static CronycleUser CurrentUser(Context ctx)
    {
    	if (currentUserInstance == null)
    	{
    		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    		
    		if (prefs.contains(PREFERENCES_KEY))
        	{
        		String json = prefs.getString(PREFERENCES_KEY, "");
        		Gson gson = new Gson();
        		currentUserInstance = gson.fromJson(json, CronycleUser.class);
        	}
    	}
    	
    	return currentUserInstance;
    }
    
    public static CronycleUser CurrentUser()
    {
    	return currentUserInstance;
    }
    
    public static void SetCurrentUser(CronycleResponseSignIn userData, Context ctx) {
    	userData.user.setAuthToken(userData.auth_token);
    	userData.user.SaveToPreferences(ctx);

		Toast.makeText(ctx, String.format("Logged in as %s", userData.user.getFull_name()), Toast.LENGTH_LONG).show();
    }

}
