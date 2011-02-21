package com.haikuwind.feed.parser;

import static com.haikuwind.feed.parser.XmlTags.FAVORITED_BY_ME;
import static com.haikuwind.feed.parser.XmlTags.POINTS;
import static com.haikuwind.feed.parser.XmlTags.TEXT;
import static com.haikuwind.feed.parser.XmlTags.TIME;
import static com.haikuwind.feed.parser.XmlTags.TIMES_VOTED_BY_ME;
import static com.haikuwind.feed.parser.XmlTags.USER;
import static com.haikuwind.feed.parser.XmlTags.USER_RANK;

import java.net.URLDecoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.UserInfo;

public class HaikuHandler extends DefaultHandler {
    private static String TAG = HaikuHandler.class.getName();

    private List<Haiku> haikuList;
    private Haiku currentHaiku;
    private UserInfo userInfo;

    public List<Haiku> getHaikuList() {
        return haikuList;
    }

    public UserInfo getUseInfo() {
        return userInfo;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (XmlTags.HAIKU.equalsIgnoreCase(localName)) {
            haikuList.add(currentHaiku);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (XmlTags.ANSWER.equalsIgnoreCase(localName)) {
            haikuList = new ArrayList<Haiku>();
        } else if (XmlTags.HAIKU.equalsIgnoreCase(localName)) {
            currentHaiku = new Haiku();

            try {
                String text = URLDecoder.decode(attributes.getValue(TEXT));
                currentHaiku.setText(text);
                currentHaiku.setFavoritedByMe(Boolean.parseBoolean(attributes
                        .getValue(FAVORITED_BY_ME)));
                currentHaiku.setPoints(Integer.parseInt(attributes
                        .getValue(POINTS)));
                currentHaiku.setTime(new Date(Long.parseLong(attributes
                        .getValue(TIME))));
                currentHaiku.setTimesVotedByMe(Integer.parseInt(attributes
                        .getValue(TIMES_VOTED_BY_ME)));
                currentHaiku.setUser(attributes.getValue(USER));
                currentHaiku.setUserRank(Integer.parseInt(attributes
                        .getValue(USER_RANK)));

                Log.d(TAG, currentHaiku.toString());
            } catch (NumberFormatException e) {
                Log.e(TAG, "incorrect value in haiku XML", e);
            }
        } else if (XmlTags.YOU.equalsIgnoreCase(localName)) {
            userInfo = new UserInfo();
            try {
                userInfo.setRank(Integer.parseInt(attributes
                        .getValue(XmlTags.RANK)));
                userInfo.setScore(Integer.parseInt(attributes
                        .getValue(XmlTags.SCORE)));
                userInfo.setFavoritedTimes(Integer.parseInt(attributes
                        .getValue(XmlTags.FAVORITED)));
            } catch (NumberFormatException e) {
                Log.e(TAG, "incorrect value in user info XML", e);
            }
        }
    }

}