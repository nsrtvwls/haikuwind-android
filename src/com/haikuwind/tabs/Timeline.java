package com.haikuwind.tabs;

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
    
    private long lastHaikuDate;
    
    
    public Timeline() {
        super(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        lastHaikuDate = 0;
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
    protected List<Haiku> fetchElements() throws FeedException {
        List<Haiku> response = HttpRequest.getTimeline(lastHaikuDate);
        
        if(response.size()>0) {
            lastHaikuDate = response.get(0).getTime().getTime();
        }
        
        return response;
    }

    @Override
    protected HaikuListData getHaikuListData() {
        return HaikuWindData.getInstance().getTimelineData();
    }
}