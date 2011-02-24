package com.haikuwind;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.feed.UserInfo;
import com.haikuwind.menu.dialogs.DialogBuilder;
import com.haikuwind.state.Event;
import com.haikuwind.state.State;
import com.haikuwind.state.StateListener;
import com.haikuwind.state.StateMachine;
import com.haikuwind.tabs.Favorites;
import com.haikuwind.tabs.HallOfFame;
import com.haikuwind.tabs.MyOwn;
import com.haikuwind.tabs.Timeline;
import com.haikuwind.tabs.TopChart;

public class HaikuWind extends TabActivity implements StateListener {

    @SuppressWarnings("unused")
    private static String TAG = HaikuWind.class.getName();

    private DialogBuilder dialogBuilder = new DialogBuilder(this);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        //TODO: remove when user info is requested separately
        return UserInfo.getCurrent()!=null;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        return dialogBuilder.buildDialog(id).create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.post_haiku:
            showDialog(DialogBuilder.POST_HAIKU);
            return true;
        case R.id.user_info:
            showDialog(DialogBuilder.USER_INFO);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void registerUser() {
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String userId = tManager.getDeviceId();

        try {
            HttpRequest.newUser(userId);
            StateMachine.processEvent(Event.REGISTERED);
        } catch(FeedException e) {
            dialogBuilder.buildDialog(DialogBuilder.ERROR_TRY_AGAIN)
                    .setPositiveButton(R.string.try_again, new OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            registerUser();
                        }
                    }).show();
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        StateMachine.clearListeners();
        StateMachine.processEvent(Event.APP_LAUNCH);
        StateMachine.addStateListener(this);
    }
    
    private void initTabs() {
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost(); // The activity TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        tabHost.clearAllTabs();
        
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, Timeline.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost
                .newTabSpec("Timeline")
                .setIndicator(res.getString(R.string.timeline),
                        res.getDrawable(R.drawable.ic_tab_timeline))
                .setContent(intent);
        tabHost.addTab(spec);

        // Top Chart
        intent = new Intent().setClass(this, TopChart.class);
        spec = tabHost
                .newTabSpec("TopChart")
                .setIndicator(res.getString(R.string.top_chart),
                        res.getDrawable(R.drawable.ic_tab_top))
                .setContent(intent);
        tabHost.addTab(spec);

        // Hall of Fame
        intent = new Intent().setClass(this, HallOfFame.class);
        spec = tabHost
                .newTabSpec("HallOfFame")
                .setIndicator(res.getString(R.string.hall_of_fame),
                        res.getDrawable(R.drawable.ic_tab_halloffame))
                .setContent(intent);
        tabHost.addTab(spec);

        // My Own
        intent = new Intent().setClass(this, MyOwn.class);
        spec = tabHost
                .newTabSpec("MyOwn")
                .setIndicator(res.getString(R.string.my_own),
                        res.getDrawable(R.drawable.ic_tab_myown))
                .setContent(intent);
        tabHost.addTab(spec);

        // Favorites
        intent = new Intent().setClass(this, Favorites.class);
        spec = tabHost
                .newTabSpec("Favorites")
                .setIndicator(res.getString(R.string.favorites),
                        res.getDrawable(R.drawable.ic_tab_favorites))
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        
        // This part may be not supported in future APIs
        TabWidget tw = getTabWidget();
        for (int i = 0; i < tw.getChildCount(); i++) {
            RelativeLayout relLayout = (RelativeLayout) tw.getChildAt(i);
            TextView tv = (TextView) relLayout.getChildAt(1);
            tv.setTextSize(11.0f); // just example
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        processState(StateMachine.getCurrentState());
    }
    
    @Override
    public void processState(State state) {
        switch(StateMachine.getCurrentState()) {
        case REGISTER:
            NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if(info==null || !info.isConnected()) {
                showDialog(DialogBuilder.SUGGEST_NETWORK_SETTINGS);
            } else {
                registerUser();
            }
            break;
            
        case INIT_LAYOUT:
            initTabs();
            StateMachine.processEvent(Event.LAYOUT_READY);
            break;
            
        case STARTED:
            break;
        }
        
    }


}