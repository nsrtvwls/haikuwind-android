package com.haikuwind.tabs;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.fetch.FeedException;
import com.haikuwind.feed.fetch.HttpRequest;
import com.haikuwind.notification.DataUpdater;
import com.haikuwind.notification.UpdateAction;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;

public class HallOfFame extends HaikuListActivity implements HasFavoriteBtn {
    private BroadcastReceiver receiver = new DataUpdater(data) {
        @Override
        protected boolean tryUpdate(UpdateAction update, Haiku haiku) {
            //if haiku was voted and got in hall of fame, set the hall of fame to update.
            return haiku.getPoints()>=Haiku.HALL_OF_FAME_POINTS;
        }
        
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(UpdateAction.VOTE.toString());
        filter.setPriority(DATA_UPDATE_PRIORITY);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getHallOfFame(getUserId());
    }
    
}