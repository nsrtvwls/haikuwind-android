package com.haikuwind.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.haikuwind.feed.HaikuListData;
import com.haikuwind.tabs.HaikuListActivity;

/**
 * Updates HaikyListActivity on event receive.
 * Activity unregisters its receiver on pause, so only currently active one is updated.
 * {@link DataUpdater} should update all {@link HaikuListData} first to mark them dirty.
 * @author oakjumper
 *
 */
public class TabUpdater extends BroadcastReceiver implements Updater {

    private final HaikuListActivity tab;

    public TabUpdater(HaikuListActivity tab) {
        this.tab = tab;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        tab.adjustView();
    }

}
