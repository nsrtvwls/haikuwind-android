package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;

public class TopChart extends HaikuListActivity implements HasVoteBtn, HasFavoriteBtn {

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getTop(25);
    }
}