package com.haikuwind.tabs;

import java.util.Arrays;
import java.util.List;

import com.haikuwind.feed.Haiku;

public class Timeline extends HaikuListActivity {

	@Override
	protected List<Haiku> fetchElements() {
		return Arrays.asList(new Haiku("old pond...\na frog leaps in\nwater’s sound",0),
				new Haiku("Over the wintry\nforest, winds howl in rage\nwith no leaves to blow.",0));
	}
}