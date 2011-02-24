package com.haikuwind.tabs;

import java.util.List;

import android.os.Bundle;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class Timeline extends HaikuListActivity {

    public Timeline() {
        super(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getTimeline(1);
    }

}