package com.haikuwind.feed.http;

enum Command {
	NEW_USER("new_user"),
	NEW_TEXT("new_text"),
	REFRESH("refresh"),
	MY("my"),
	VOTE("vote"),
	FAVORITE("favorite"),
	MY_FAVORITE("my_favorite"),
	TOP("top");
	
	private final String id;

	private Command(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}

}
