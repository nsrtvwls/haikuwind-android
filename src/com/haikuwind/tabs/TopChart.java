package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class TopChart extends HaikuListActivity {

    public TopChart() {
        super(false);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getTop(25);
    }
}