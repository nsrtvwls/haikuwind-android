package com.haikuwind.tabs;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;

public abstract class HaikuListActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.haiku_list);
        
        LinearLayout haikuList = (LinearLayout)findViewById(R.id.haiku_list); 
        
        for(Haiku h: fetchElements()) {
	        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        TextView haikuView = (TextView) inflater.inflate(R.layout.haiku, null);
	        haikuView.setText(h.getText());
	        
	        haikuList.addView(haikuView);
        }
    }
    
    abstract protected List<Haiku> fetchElements(); 
}