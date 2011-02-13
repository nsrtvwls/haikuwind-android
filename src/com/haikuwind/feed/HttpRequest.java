package com.haikuwind.feed;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;


public class HttpRequest {
	private final static String HW_ADDR = "http://localhost/haiku";
	
	public static List<Haiku> getTimeline(String userId, Date from) {
		try {
			String t_url = String.format("%s?command=refresh&user=%s&from=%d", HW_ADDR, userId, from.getTime());
			URL url = new URL(t_url);
			
			InputStream is = url.openStream();
			try{
				return HaikuParser.parse(is);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			//TODO throw custom exception
			return null;
		}
	}
}
