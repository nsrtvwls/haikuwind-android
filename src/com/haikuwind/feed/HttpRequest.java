package com.haikuwind.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

public class HttpRequest {
	private static String TAG = HttpRequest.class.getName();
	
	private final static String HW_ADDR = "http://192.168.4.134:8888/haiku";

	public static List<Haiku> getTimeline(String userId, long from) {
//		http://localhost:8080/haiku?command=refresh&user=1&from=1
		String url = String.format("%s?command=refresh&user=%s&from=%d",
				HW_ADDR, userId, from);
		return parse(url);
	}
	
	public static List<Haiku> getTop(String userId, int limit) {
//		http://localhost:8080/haiku?command=top&user=1&limit=25
		String url = String.format("%s?command=top&user=%s&limit=%d", 
				HW_ADDR, userId, limit);
		return parse(url);
	}

	public static List<Haiku> getHallOfFame(String userId) {
//		http://localhost:8080/haiku?command=hall_of_fame&user=1
		String url = String.format("%s?command=hall_of_fame&user=%s", 
				HW_ADDR, userId);
		return parse(url);
	}
	
	public static List<Haiku> getFavorite(String userId) {
//		http://localhost:8080/haiku?command=my_favorite&user=1
		String url = String.format("%s?command=my_favorite&user=%s", 
				HW_ADDR, userId);
		return parse(url);
	}
	
	public static List<Haiku> getMy(String userId) {
//		http://localhost:8080/haiku?command=my&user=1
		String url = String.format("%s?command=my&user=%s", 
				HW_ADDR, userId);
		return parse(url);
	}
	
	private static List<Haiku> parse(String url) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			HaikuHandler handler = new HaikuHandler();
			xr.setContentHandler(handler);

			Log.d(TAG, url);
			InputStream haikuStream = new URL(url).openStream();
			try {
				xr.parse(new InputSource(haikuStream));
			} finally {
				haikuStream.close();
			}

			return handler.getHaikuList();

		} catch (IOException e) {
			//TODO show error message
			Log.e(TAG, "Connection error", e);
			return Collections.EMPTY_LIST;
		} catch (Exception e) {
			Log.e(TAG, "Connection error", e);
			return Collections.EMPTY_LIST;
		}
	}
}
