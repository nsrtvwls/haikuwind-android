package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.fetch.FeedException;
import com.haikuwind.feed.fetch.HttpRequest;
import com.haikuwind.notification.UpdateNotifier;
import com.haikuwind.notification.UpdateType;
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class MyOwn extends HaikuListActivity implements HasVoteBtn {

    @Override
    protected void onStart() {
        super.onStart();
        UpdateNotifier.addUpdateListener(this, UpdateType.NEW_HAIKU);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateNotifier.removeUpdateListener(this);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getMy(getUserId());
    }

}