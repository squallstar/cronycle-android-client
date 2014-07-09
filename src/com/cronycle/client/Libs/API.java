package com.cronycle.client.Libs;

import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.util.Log;

public class API {
	
	private static CronycleApiInterface sCronycleService;
	private static API instance;
	
	Twitter twitter;
	RequestToken requestToken;

	public final static String consumerKey = "qviFYtnlxu45G8mf1NuC6g";
	public final static String consumerSecret = "bc9Swfbv6mt8rKLwecYjSUUFsfgCYcxPYuKvRTrLous";
	public final static String CALLBACKURL = "cronycle://twitter";
	private OnFetchListener cb;
	private OnFetchListener cb2;

	public static API Current() {
		if (instance == null) instance = new API();
		return instance;
	}
	
    public static CronycleApiInterface getCronycleApiClient() {
        if (sCronycleService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.cronycle.com")
                    .build();
            
            Log.i("API", "REST Service created");

            sCronycleService = restAdapter.create(CronycleApiInterface.class);
        }

        return sCronycleService;
    }
    
    public interface OnFetchListener {
        void onComplete(Boolean success);
    }
    
    /*
	 * - Creates object of Twitter and sets consumerKey and consumerSecret
	 * - Prepares the URL accordingly and opens the WebView for the user to provide sign-in details
	 * - When user finishes signing-in, WebView opens your activity back
	 */
	public String getOAuthLoginUrl() {
		try {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
			
			requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
			return requestToken.getAuthenticationURL();
		} catch (TwitterException ex) {
			Log.e("in API.getOAuthLoginUrl", ex.getMessage());
		}
		
		return null;
	}
	
	public AccessToken getAccessToken(String verifier) {
		try {
			return twitter.getOAuthAccessToken(requestToken, verifier);
		} catch (TwitterException ex) {
			Log.e("in API.getAccessToken", ex.getMessage());
		}
		
		return null;
	}
	
	public void loadLinksForCollection(final CronycleCollection collection, final OnFetchListener cb) {
		
		Thread thread = new Thread(new Runnable() {
    		public void run() {
    			String max_timestamp = "";
    			
    			if (collection.links.size() > 0) {
    				max_timestamp = String.format(Locale.ENGLISH, "%d", collection.links.get(collection.links.size()-1).published_at-1);
    			}
    			
    			getCronycleApiClient().getCollectionLinks(collection.private_id, max_timestamp, CronycleUser.CurrentUser().getAuthToken(), new Callback<CronycleLink[]>() {

					@Override
					public void failure(RetrofitError arg0) {
						cb.onComplete(false);
					}

					@Override
					public void success(CronycleLink[] new_links, Response arg1) {
						for(int x = 0; x < new_links.length; x = x+1) {
							collection.links.add(new_links[x]);
						}
						
						cb.onComplete(true);
					}
				
    			});
    		}
		});
		
		thread.start();
	}

    public interface CronycleApiInterface {
        @GET("/v3/user.json")
        void getUser(@Query("auth_token") String auth_token, Callback<CronycleUser> callback);
        
        @POST("/v3/sign_in.json")
    	void twitterSignIn(@Body CronycleRequestSignIn userData, Callback<CronycleResponseSignIn> callback);
        
        @GET("/v3/collections.json")
        void getUserCollections(
        		@Query("include_links") Boolean include_links,
        		@Query("include_first") int include_first,
        		@Query("auth_token") String auth_token,
        		Callback<CronycleCollection[]> callback
        );
        
        @GET("/v3/collections/{private_id}/links.json")
        void getCollectionLinks(
        		@Path("private_id") String private_id,
        		@Query("max_timestamp") String max_timestamp,
        		@Query("auth_token") String auth_token,
        		Callback<CronycleLink[]> callback
        );
    }	
}
