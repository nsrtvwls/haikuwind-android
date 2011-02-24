package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class MyOwn extends HaikuListActivity {

    public MyOwn() {
        super(false);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getMy();
    }
}