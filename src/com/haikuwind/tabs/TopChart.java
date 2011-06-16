package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HaikuListData;
import com.haikuwind.feed.HaikuWindData;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class TopChart extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {

    @Override
    protected void onStart() {
        super.onStart();
        UpdateNotifier.addUpdateListener(this, Update.VOTE);
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
    protected HaikuListData getHaikuListData() {
        return HaikuWindData.getInstance().getTopChartData();
    }
    
    @Override
    public void processUpdate(Update update, Haiku haiku) {
        if(Update.VOTE==update) {
            List<Haiku> topChartList = getHaikuListData().getHaikuList();
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