package com.haikuwind.tabs;

import java.util.List;

import android.os.Bundle;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;

public class Timeline extends VotableHaikuList {
    @SuppressWarnings("unused")
    private final static String TAG = Timeline.class.getSimpleName();
    
    private long lastHaikuDate;
    
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
        List<Haiku> result = HttpRequest.getTimeline(lastHaikuDate);
        lastHaikuDate = result.get(result.size()-1).getTime().getTime();
        return result;
    }


}