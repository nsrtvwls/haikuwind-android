package com.haikuwind.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.haikuwind.UserInfoHolder;
import com.haikuwind.feed.parser.HaikuHandler;
import com.haikuwind.feed.parser.ResultHandler;

import android.util.Log;

public class HttpRequest {
	private static String TAG = HttpRequest.class.getName();
	
	private final static String HW_ADDR = "http://192.168.4.134:8888/haiku";

	public static boolean newUser(String userId) {
//	    http://localhost:8080/haiku?command=new_user&id=ABCD
		String url = String.format("%s?command=new_user&id=%s",
				HW_ADDR, userId);
		return parseResult(url);
	}
	
	public static boolean newHaiku(String userId, CharSequence haiku) {
//		http://localhost:8080/haiku?command=new_text&user=1&haiku=.......
		String url = String.format("%s?command=new_text&user=%s&haiku=%s",
				HW_ADDR, userId, URLEncoder.encode(haiku.toString()));
		return parseResult(url);
	}

	public static List<Haiku> getTimeline(String userId, long from) {
//		http://localhost:8080/haiku?command=refresh&user=1&from=1
		String url = String.format("%s?command=refresh&user=%s&from=%d",
				HW_ADDR, userId, from);
		return parseHaikuList(url);
	}
	
	public static List<Haiku> getTop(String userId, int limit) {
//		http://localhost:8080/haiku?command=top&user=1&limit=25
		String url = String.format("%s?command=top&user=%s&limit=%d", 
				HW_ADDR, userId, limit);
		return parseHaikuList(url);
	}

	public static List<Haiku> getHallOfFame(String userId) {
//		http://localhost:8080/haiku?command=hall_of_fame&user=1
		String url = String.format("%s?command=hall_of_fame&user=%s", 
				HW_ADDR, userId);
		return parseHaikuList(url);
	}
	
	public static List<Haiku> getFavorite(String userId) {
//		http://localhost:8080/haiku?command=my_favorite&user=1
		String url = String.format("%s?command=my_favorite&user=%s", 
				HW_ADDR, userId);
		return parseHaikuList(url);
	}
	
	public static List<Haiku> getMy(String userId) {
//		http://localhost:8080/haiku?command=my&user=1
		String url = String.format("%s?command=my&user=%s", 
				HW_ADDR, userId);
		return parseHaikuList(url);
	}
	
	
	/**
	 * Side effect is: if user info received, it updates {@link UserInfoHolder}
	 */
	private static List<Haiku> parseHaikuList(String url) {
		try {
			HaikuHandler handler = new HaikuHandler();
			parse(url, handler);
			
			UserInfo user = handler.getUseInfo(); 
			if(user!=null) {
				UserInfoHolder.setUserInfo(user);
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
	
	private static boolean parseResult(String url) {
		try {
			ResultHandler handler = new ResultHandler();
			parse(url, handler);
			return handler.getResult();

		} catch (IOException e) {
			//TODO show error message
			Log.e(TAG, "Connection error", e);
			return false;
		} catch (Exception e) {
			Log.e(TAG, "Connection error", e);
			return false;
		}
	}
	
	private static void parse(String url, ContentHandler handler) throws ParserConfigurationException, SAXException, MalformedURLException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		xr.setContentHandler(handler);

		Log.d(TAG, url);
		InputStream xmlStream = new URL(url).openStream();
		try {
			xr.parse(new InputSource(xmlStream));
		} finally {
			xmlStream.close();
		}
	}
}
