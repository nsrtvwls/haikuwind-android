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
		String url = String.format("%s?command=refresh&user=%s&from=%d",
				HW_ADDR, userId, from);
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
