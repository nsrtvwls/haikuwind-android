package com.haikuwind.feed.parser;

public interface XmlTags {

	String ANSWER = "answer";
	
	//Result attribute and its codes
	String RESULT = "result";
	String SUCCESS = "success";
	String ERROR = "error";
	
	//Haiku element and its attributes
	String HAIKU = "haiku";
	String TEXT = "text";
	String POINTS = "points";
	String USER = "user";
	String USER_RANK = "userRank";
	String FAVORITED_BY_ME = "favoritedByMe";
	String TIMES_VOTED_BY_ME = "timesVotedByMe";
	String TIME = "time"; 

}
