package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class HallOfFame extends HaikuListActivity {

	public HallOfFame() {
        super(false);
    }

    @Override
	protected List<Haiku> fetchElements() throws FeedException {
		return HttpRequest.getHallOfFame();
	}
}