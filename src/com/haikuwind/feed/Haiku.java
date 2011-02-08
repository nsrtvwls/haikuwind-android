package com.haikuwind.feed;

public class Haiku {
	private final String text;
	private int counter;

	public Haiku(String text, int counter) {
		this.text = text;
		this.counter = counter;
	}

	public int getCounter() {
		return counter;
	}

	public String getText() {
		return text;
	}
	
}
