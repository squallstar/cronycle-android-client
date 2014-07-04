package com.cronycle.client.Libs;

import android.util.Log;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

public class API {
	
	private static CronycleApiInterface sCronycleService;

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

    public interface CronycleApiInterface {
        @GET("/v3/user.json?auth_token=3e1bd989408c45d9")
        void getUser(@Query("limit") int limit, Callback<CronycleUserData> callback);
    }
	
}
