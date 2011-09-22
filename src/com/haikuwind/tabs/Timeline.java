package com.haikuwind.tabs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.fetch.FeedException;
import com.haikuwind.feed.fetch.HttpRequest;
import com.haikuwind.notification.DataUpdater;
import com.haikuwind.notification.UpdateAction;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class Timeline extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {
    @SuppressWarnings("unused")
    private final static String TAG = Timeline.class.getSimpleName();
    private BroadcastReceiver receiver = new DataUpdater(data);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        IntentFilter filter = new IntentFilter(UpdateAction.NEW_HAIKU.toString());
        filter.setPriority(DATA_UPDATE_PRIORITY);
        registerReceiver(receiver , filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
    
    @Override
    protected boolean eraseOldHaikus() {
        //full update only for the first load
        return data.getUpdateCounter()==0;
    }
    
    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        Date lastHaikuDate = data.getLastHaikuDate();
        List<Haiku> response = HttpRequest.getTimeline(
                lastHaikuDate==null ? monthBefore().getTime(): lastHaikuDate.getTime(),
                getUserId());
        
        return response;
    }
    
    private Date monthBefore() {
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.DATE, -30);
        return weekBefore.getTime();
    }

}