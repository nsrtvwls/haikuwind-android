package com.haikuwind.feed;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.haikuwind.tabs.Favorites;
import com.haikuwind.tabs.HaikuListActivity;
import com.haikuwind.tabs.HallOfFame;
import com.haikuwind.tabs.MyOwn;
import com.haikuwind.tabs.Timeline;
import com.haikuwind.tabs.TopChart;


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
    
    private final Map<Class<? extends HaikuListActivity>, HaikuListData> lists;
    
    public HaikuWindData() {
        Map<Class<? extends HaikuListActivity>, HaikuListData> map = 
                new HashMap<Class<? extends HaikuListActivity>, HaikuListData>();
        
        map.put(Timeline.class, new HaikuListData());
        map.put(Favorites.class, new HaikuListData());
        map.put(HallOfFame.class, new HaikuListData());
        map.put(TopChart.class, new HaikuListData());
        map.put(MyOwn.class, new HaikuListData());
        
        lists = Collections.unmodifiableMap(map);
    }

    synchronized public boolean isRegistered() {
        return registered;
    }
    synchronized public void setRegistered(boolean registered) {
        this.registered = registered;
    }
    
    public Map<Class<? extends HaikuListActivity>, HaikuListData> getLists() {
        return lists;
    }
    
    synchronized public UserInfo getUserInfo() {
        return userInfo;
    }
    synchronized public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void resetLists() {
        for(HaikuListData data: lists.values()) {
            data.setDataInvalid();
        }
        
        lastUpdateTime = new Date();
    }
    
    public boolean isDataObsolete() {
        Calendar firstValidTime = Calendar.getInstance();
        firstValidTime.add(Calendar.MINUTE, -UPDATE_PERIOD);
        
        return lastUpdateTime==null || lastUpdateTime.before(firstValidTime.getTime());
    }
    
    public HaikuListData getHaikuListData(Class<? extends HaikuListActivity> tab) {
        return lists.get(tab);
    }
    
}
