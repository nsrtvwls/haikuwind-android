package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class Timeline extends VotableHaikuList {
    @SuppressWarnings("unused")
    private final static String TAG = Timeline.class.getSimpleName();

    @Override
    protected List<Haiku> fetchElements() throws FeedException {
        return HttpRequest.getTimeline(1);
    }

}