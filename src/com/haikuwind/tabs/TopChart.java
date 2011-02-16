package com.haikuwind.tabs;

import java.util.List;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class TopChart extends HaikuListActivity {

	@Override
	protected List<Haiku> fetchElements() {
		return HttpRequest.getTop(getUserId(), 25);
	}
}