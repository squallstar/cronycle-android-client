package com.cronycle.client.Libs;

public class CronycleRequestSignIn {
	
	private User user;
	
	public static class User {
		
		public String email;
		public String password;
		
		public TwitterUser twitter;
		
		public User() {
			twitter = new TwitterUser();
		}
		
		public User(String email, String password) {
			this.email = email;
			this.password = password;
		}
		
		public static class TwitterUser {
			public long uid;
			public String token;
			public String secret;
		}	
	}
	
	/**
	 * Twitter Sign in
	 * @param uid
	 * @param token
	 * @param secret
	 */
	public CronycleRequestSignIn(long uid, String token, String secret) {
		user = new User();
		user.twitter.uid = uid;
		user.twitter.token = token;
		user.twitter.secret = secret;
	}
	
	/**
	 * Traditional sign in
	 * @param email
	 * @param password
	 */
	public CronycleRequestSignIn(String email, String password) {
		user = new User(email, password);
	}
	
}
