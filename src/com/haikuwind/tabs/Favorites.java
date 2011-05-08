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

public class Favorites extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {

    @Override
    protected void onStart() {
        super.onStart();
        UpdateNotifier.addUpdateListener(this, Update.ADD_FAVORITE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateNotifier.removeUpdateListener(this);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getFavorite();
    }

    @Override
    protected HaikuListData getHaikuListData() {
        return HaikuWindData.getInstance().getFavoritesData();
    }
}