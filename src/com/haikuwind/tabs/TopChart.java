package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.fetch.FeedException;
import com.haikuwind.feed.fetch.HttpRequest;
import com.haikuwind.notification.UpdateNotifier;
import com.haikuwind.notification.UpdateType;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class TopChart extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {

    @Override
    protected void onPause() {
        super.onPause();
    }
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
        return HttpRequest.getTop(25, getUserId());
    }

    @Override
    public void processUpdate(UpdateType update, Haiku haiku) {
        if(UpdateType.VOTE==update) {
            List<Haiku> topChartList = data.getHaikuList();
            if(topChartList.size()>0) {
                Haiku minPointsHaiku = topChartList.get(topChartList.size()-1);
                if(minPointsHaiku.getPoints()>haiku.getPoints()) {
                    //voted haiku doesn't affect top chart, skip update
                    return;
                }
            }
        }
        
        super.processUpdate(update, haiku);
    }
}