package com.haikuwind.feed.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ResultHandler extends DefaultHandler {
	private boolean result;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (XmlTags.ANSWER.equalsIgnoreCase(localName)) {
			String resultCode = attributes.getValue(XmlTags.RESULT);
			result = XmlTags.SUCCESS.equalsIgnoreCase(resultCode);
		}
	}
	
	public boolean getResult() {
		return result;
	}
}
