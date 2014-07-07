package com.cronycle.client.Libs;

public class CronycleRequestSignIn {
	
	private User user;
	
	public static class User {
		
		public TwitterUser twitter;
		
		public User() {
			twitter = new TwitterUser();
		}
		
		public static class TwitterUser {
			public long uid;
			public String token;
			public String secret;
		}	
	}
	
	public CronycleRequestSignIn(long uid, String token, String secret) {
		user = new User();
		user.twitter.uid = uid;
		user.twitter.token = token;
		user.twitter.secret = secret;
	}
	
}
