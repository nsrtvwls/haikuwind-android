package com.haikuwind;

import android.util.Log;

public class UserIdHolder {
	
	private static String userId;
	private static String TAG = UserIdHolder.class.getName();
	
	static void init(String userId) {
		UserIdHolder.userId = userId;
	}
	
	public static String getUserId() {
		Log.d(TAG , "User ID: "+userId);
		return userId;
	}

}
