package com.haikuwind.tabs;

import java.util.List;

import android.os.Bundle;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

public class Timeline extends HaikuListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected List<Haiku> fetchElements() {
		return HttpRequest.getTimeline(getUserId(), 1);
	}
	
	
}