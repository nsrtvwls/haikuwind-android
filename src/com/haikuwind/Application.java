package com.haikuwind;

import com.haikuwind.feed.HaikuWindData;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HaikuWindData.getInstance().resetLists();
    }
}
