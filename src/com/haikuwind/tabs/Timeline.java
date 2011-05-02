package com.haikuwind.tabs;

import java.util.List;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.haikuwind.R;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class Timeline extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {
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
        if(!result.isEmpty()) {
            lastHaikuDate = result.get(0).getTime().getTime();
        }
        return result;
    }

    @Override
    protected void renderNewHaiku(List<Haiku> haikuResponse) {
        //do not remove old
        LinearLayout haikuListView = (LinearLayout) findViewById(R.id.haiku_list);
        
        for (int i=0; i<haikuResponse.size(); ++i) {
            ViewGroup haikuView = createSingleHaikuWidget(haikuResponse.get(i));
            haikuListView.addView(haikuView, i);

        }
    }

    @Override
    protected void updateStored(List<Haiku> haikuResponse) {
        //do not clear old
        for(Haiku h: haikuResponse) {
            haikuMap.put(h.getId(), h);
        }
    }

    
}