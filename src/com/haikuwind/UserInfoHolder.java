package com.haikuwind;

import com.haikuwind.feed.UserInfo;

import android.util.Log;

public class UserInfoHolder {
    private static String TAG = UserInfoHolder.class.getName();

    private static String userId;
    private static UserInfo userInfo;

    static void registerUser(String userId) {
        UserInfoHolder.userId = userId;
    }

    public static String getUserId() {
        Log.d(TAG, "User ID: " + userId);
        return userId;
    }

    synchronized public static void setUserInfo(UserInfo info) {
        userInfo = info;
    }

    synchronized public static UserInfo getUserInfo() {
        return userInfo;
    }
}
