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
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class TopChart extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {
    
    private BroadcastReceiver receiver = new DataUpdater(data) {

        @Override
        protected boolean tryUpdate(UpdateAction update, Haiku haiku) {
            List<Haiku> topChartList = data.getHaikuList();
            if(topChartList.size()>0) {
                Haiku minPointsHaiku = topChartList.get(topChartList.size()-1);
                if(minPointsHaiku.getPoints()>haiku.getPoints()) {
                    //voted haiku doesn't affect top chart, skip update
                    return false;
                }
            }
            
            return true;
        }
        
    };

    @Override
    protected void onPause() {
        super.onPause();
    }
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
        return HttpRequest.getTop(25, getUserId());
    }

}