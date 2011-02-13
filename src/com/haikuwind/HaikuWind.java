package com.haikuwind;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.haikuwind.tabs.Favorites;
import com.haikuwind.tabs.HallOfFame;
import com.haikuwind.tabs.MyOwn;
import com.haikuwind.tabs.Timeline;
import com.haikuwind.tabs.TopChart;

public class HaikuWind extends TabActivity {
	private static String TAG = HaikuWind.class.getName();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, Timeline.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("Timeline").setIndicator(res.getString(R.string.timeline),
                          res.getDrawable(R.drawable.ic_tab_timeline))
                      .setContent(intent);
        tabHost.addTab(spec);

        //Top Chart
        intent = new Intent().setClass(this, TopChart.class);
        spec = tabHost.newTabSpec("TopChart").setIndicator(res.getString(R.string.top_chart),
                          res.getDrawable(R.drawable.ic_tab_top))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        //Hall of Fame
        intent = new Intent().setClass(this, HallOfFame.class);
        spec = tabHost.newTabSpec("HallOfFame").setIndicator(res.getString(R.string.hall_of_fame),
                          res.getDrawable(R.drawable.ic_tab_halloffame))
                      .setContent(intent);
        tabHost.addTab(spec);

        //My Own
        intent = new Intent().setClass(this, MyOwn.class);
        spec = tabHost.newTabSpec("MyOwn").setIndicator(res.getString(R.string.my_own),
                          res.getDrawable(R.drawable.ic_tab_myown))
                      .setContent(intent);
        tabHost.addTab(spec);

        //Favorites
        intent = new Intent().setClass(this, Favorites.class);
        spec = tabHost.newTabSpec("Favorites").setIndicator(res.getString(R.string.favorites),
                          res.getDrawable(R.drawable.ic_tab_favorites))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        
        //This part may be not supported in future APIs
        TabWidget tw = getTabWidget();
        for (int i=0; i<tw.getChildCount(); i++) {
            RelativeLayout relLayout = (RelativeLayout)tw.getChildAt(i);
            TextView tv = (TextView)relLayout.getChildAt(1);
            tv.setTextSize(11.0f); // just example
        }     
    }
    
}