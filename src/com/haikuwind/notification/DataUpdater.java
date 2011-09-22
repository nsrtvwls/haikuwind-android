package com.haikuwind.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HaikuListData;

/**
 * Sets data dirty for all received update types.
 * @author oakjumper
 *
 */
public class DataUpdater extends BroadcastReceiver implements Updater {

    @SuppressWarnings("unused")
    private static String TAG = DataUpdater.class.getSimpleName();
    
    private final HaikuListData data;

    public DataUpdater(HaikuListData data) {
        this.data = data;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateAction update = UpdateAction.fromName(intent.getAction());
        Haiku haiku = (Haiku) intent.getSerializableExtra(HAIKU_KEY);
        
        data.setDataDirty(tryUpdate(update, haiku));
    }

    /**
     * @param update update type
     * @param haiku updated hailu or <code>null</code>
     * @return if this update invalidates haiku list
     */
    protected boolean tryUpdate(UpdateAction update, Haiku haiku) {
        return true;
    }

}
