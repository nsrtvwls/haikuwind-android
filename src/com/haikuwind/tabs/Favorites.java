package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class Favorites extends HaikuListActivity {

    public Favorites() {
        super(false);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getFavorite();
    }
}