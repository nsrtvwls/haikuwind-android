package com.haikuwind.feed;

import java.util.Date;

public class Haiku implements Cloneable {
	private String text;
	private int points;
	private String user;
	private int userRank;
	private boolean favoritedByMe;
	private int timesVotedByMe;
	private Date time;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getUserRank() {
		return userRank;
	}
	public void setUserRank(int userRank) {
		this.userRank = userRank;
	}
	public boolean isFavoritedByMe() {
		return favoritedByMe;
	}
	public void setFavoritedByMe(boolean favoritedByMe) {
		this.favoritedByMe = favoritedByMe;
	}
	public int getTimesVotedByMe() {
		return timesVotedByMe;
	}
	public void setTimesVotedByMe(int timesVotedByMe) {
		this.timesVotedByMe = timesVotedByMe;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	protected Haiku clone() {
		try {
			return (Haiku) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("This could never happen. Haiku is Cloneable");
		}
	}
	@Override
	public String toString() {
		return "Haiku [text=" + text + ", points=" + points + ", user=" + user
				+ ", userRank=" + userRank + ", favoritedByMe=" + favoritedByMe
				+ ", timesVotedByMe=" + timesVotedByMe + ", time=" + time + "]";
	}
	
}
