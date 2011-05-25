package com.haikuwind.feed;


public class HaikuWindData {

    private static HaikuWindData instance = new HaikuWindData();
    
    public static HaikuWindData getInstance() {
        return instance;
    }
    
    private boolean registered = false;


    private UserInfo userInfo;
    
    private HaikuListData timelineData   = new HaikuListData();
    private HaikuListData favoriteData   = new HaikuListData();
    private HaikuListData hallOfFameData = new HaikuListData();
    private HaikuListData topChartData   = new HaikuListData();
    private HaikuListData myOwnData      = new HaikuListData();

    synchronized public boolean isRegistered() {
        return registered;
    }
    synchronized public void setRegistered(boolean registered) {
        this.registered = registered;
    }
    synchronized public UserInfo getUserInfo() {
        return userInfo;
    }
    synchronized public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public HaikuListData getTimelineData() {
        return timelineData;
    }
    public HaikuListData getFavoritesData() {
        return favoriteData;
    }
    public HaikuListData getHallOfFameData() {
        return hallOfFameData;
    }
    public HaikuListData getTopChartData() {
        return topChartData;
    }
    public HaikuListData getMyOwnData() {
        return myOwnData;
    }
    
}
