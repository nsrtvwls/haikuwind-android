package com.haikuwind.tabs;

import java.util.Collections;
import java.util.List;

import com.haikuwind.feed.Haiku;

public class HallOfFame extends HaikuListActivity {

	@Override
	protected List<Haiku> fetchElements() {
		return Collections.EMPTY_LIST;
	}
}