package com.haikuwind;

import com.haikuwind.feed.HaikuWindData;

/**
 * forget fetched haikus on application restart
 * to make sure all lists are initialized the same way.
 * @author oakjumper
 *
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HaikuWindData.getInstance().resetLists();
    }
}
