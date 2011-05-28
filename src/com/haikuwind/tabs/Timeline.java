package com.haikuwind.tabs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HaikuListData;
import com.haikuwind.feed.HaikuWindData;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class Timeline extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {
    @SuppressWarnings("unused")
    private final static String TAG = Timeline.class.getSimpleName();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        UpdateNotifier.addUpdateListener(this, Update.NEW_HAIKU);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateNotifier.removeUpdateListener(this);
    }
    
    @Override
    protected boolean eraseOldHaikus() {
        //full update only for the first load
        return getHaikuListData().getUpdateCounter()==0;
    }
    
    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        Date lastHaikuDate = getHaikuListData().getLastHaikuDate();
        List<Haiku> response = HttpRequest.getTimeline(
                lastHaikuDate==null ? weekBefore().getTime(): lastHaikuDate.getTime());
        
        return response;
    }
    
    private Date weekBefore() {
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.DATE, -7);
        return weekBefore.getTime();
    }

    @Override
    protected HaikuListData getHaikuListData() {
        return HaikuWindData.getInstance().getTimelineData();
    }
}