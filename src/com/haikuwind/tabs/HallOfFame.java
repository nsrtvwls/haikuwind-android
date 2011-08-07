package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.fetch.FeedException;
import com.haikuwind.feed.fetch.HttpRequest;
import com.haikuwind.notification.UpdateNotifier;
import com.haikuwind.notification.UpdateType;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;

public class HallOfFame extends HaikuListActivity implements HasFavoriteBtn {

    @Override
    protected void onStart() {
        super.onStart();
        UpdateNotifier.addUpdateListener(this, UpdateType.VOTE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateNotifier.removeUpdateListener(this);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getHallOfFame(getUserId());
    }
    
    @Override
    public void processUpdate(UpdateType update, Haiku haiku) {
        //if haiku was voted and got in hall of fame, mark the hall of fame to update.
        if(update==UpdateType.REFRESH || haiku.getPoints()>=Haiku.HALL_OF_FAME_POINTS) {
            super.processUpdate(update, haiku);
        }
    }

}