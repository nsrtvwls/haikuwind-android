package com.haikuwind.tabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;

public class Timeline extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.haiku_list);
        
        LinearLayout haikuList = (LinearLayout)findViewById(R.id.haiku_list); 
        
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView haiku = (TextView) inflater.inflate(R.layout.haiku, null);
        haiku.setText("haiku1\nlalala\ntralala");
        
        haikuList.addView(haiku);
    }
}