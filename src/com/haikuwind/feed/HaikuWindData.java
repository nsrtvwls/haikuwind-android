package com.haikuwind.feed;

import java.util.Calendar;
import java.util.Date;


public class HaikuWindData {
    /**
     * period of full update of all lists
     */
    private static final int UPDATE_PERIOD = 15;

    private static HaikuWindData instance = new HaikuWindData();
    
    public static HaikuWindData getInstance() {
        return instance;
    }
    
    private boolean registered = false;

    /**
     * date of the last full update of all lists
     */
    private Date lastUpdateTime;

    private UserInfo userInfo;
    
    private final HaikuListData timelineData   = new HaikuListData();
    private final HaikuListData favoriteData   = new HaikuListData();
    private final HaikuListData hallOfFameData = new HaikuListData();
    private final HaikuListData topChartData   = new HaikuListData();
    private final HaikuListData myOwnData      = new HaikuListData();

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
    
    public void resetLists() {
        for(HaikuListData data: new HaikuListData[] {
                timelineData, favoriteData, hallOfFameData, topChartData, myOwnData
                }) {
            data.resetList();
        }
        
        lastUpdateTime = new Date();
    }
    
    public boolean isDataObsolete() {
        Calendar firstValidTime = Calendar.getInstance();
        firstValidTime.add(Calendar.MINUTE, -UPDATE_PERIOD);
        
        return lastUpdateTime==null || lastUpdateTime.before(firstValidTime.getTime());
    }
    
}
