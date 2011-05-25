package com.haikuwind;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.haikuwind.dialogs.CancelListener;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HaikuWindData;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.feed.UserInfo;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;
import com.haikuwind.tabs.Favorites;
import com.haikuwind.tabs.HallOfFame;
import com.haikuwind.tabs.MyOwn;
import com.haikuwind.tabs.Timeline;
import com.haikuwind.tabs.TopChart;

public class HaikuWind extends TabActivity {

    private static final int ERROR_TRY_AGAIN_POST_HAIKU = 1;
    private static final int POST_HAIKU = 2;
    private static final int USER_INFO = 3;
    
    @SuppressWarnings("unused")
    private static String TAG = HaikuWind.class.getSimpleName();
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        //TODO: remove when user info is requested separately
        return HaikuWindData.getInstance().getUserInfo()!=null;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout;

        switch (id) {
        case ERROR_TRY_AGAIN_POST_HAIKU:
            builder.setMessage(R.string.oops)
                    .setNegativeButton(R.string.cancel, new CancelListener())
                    .setPositiveButton(R.string.try_again,
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    showDialog(POST_HAIKU);
                                }
                            });
            break;

        case POST_HAIKU:
            layout = inflater.inflate(R.layout.post_haiku_dialog, null);

            builder.setNegativeButton(R.string.cancel, new CancelListener())
                    .setPositiveButton(R.string.send,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    doPostHaiku(dialog);
                                }
                            });

            builder.setView(layout);
            break;

        case USER_INFO:
            layout = inflater.inflate(R.layout.user_info_dialog, null);

            UserInfo user = HaikuWindData.getInstance().getUserInfo();

            ((TextView) layout.findViewById(R.id.user_rank)).setText(user
                    .getRank().getRankStringId());
            String value = Integer.toString(user.getRank().getPower());
            ((TextView) layout.findViewById(R.id.user_voting_power))
                    .setText(value);
            value = Integer.toString(user.getScore());
            ((TextView) layout.findViewById(R.id.user_score)).setText(value);
            value = Integer.toString(user.getFavoritedTimes()) + " "
                    + getString(R.string.times);
            ((TextView) layout.findViewById(R.id.user_favorited_times))
                    .setText(value);

            ((ImageView) layout.findViewById(R.id.user_image))
                    .setImageResource(user.getRank().getImageId());

            builder.setNegativeButton(R.string.close, new CancelListener())
                    .setView(layout);
            break;

        default:
            return super.onCreateDialog(id);
        }

        return builder.create();
    }

    private void doPostHaiku(DialogInterface dialog) {
        View haikuTextView = ((Dialog) dialog).findViewById(R.id.haiku_text);
        CharSequence haiku = ((TextView) haikuTextView).getText();
        
        try {
            HttpRequest.newHaiku(haiku);
            ((TextView) haikuTextView).setText("");
            
            UpdateNotifier.fireUpdate(Update.NEW_HAIKU, new Haiku(haiku.toString()));
        } catch(FeedException e) {
            //TODO how to show the same dialog that is open now?
            onCreateDialog(ERROR_TRY_AGAIN_POST_HAIKU).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.post_haiku:
            showDialog(POST_HAIKU);
            return true;
        case R.id.user_info:
            showDialog(USER_INFO);
            return true;
        case R.id.refresh:
            HaikuWindData.getInstance().resetLists();
            UpdateNotifier.fireUpdate(Update.REFRESH, null);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        initTabs();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    
    private void initTabs() {
        Log.d(TAG, "initialize tabs");
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost(); // The activity TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab
        
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
        
        Log.d(TAG, "Tabs initialized");
    }

}