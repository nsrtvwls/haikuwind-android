package com.haikuwind.tabs;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;

public abstract class HaikuListActivity extends Activity {
	private final static String TAG = HaikuListActivity.class.getName();
	private String userId;
	
	protected String getUserId() {
		return userId;
	}
	
	//TODO is it possible doing it once?
	private void initUserId() {
		TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		userId = tManager.getDeviceId();
		Log.d(TAG, "User ID: "+userId);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initUserId();
        
        setContentView(R.layout.haiku_list);
        
        LinearLayout haikuList = (LinearLayout)findViewById(R.id.haiku_list); 
        
        for(Haiku h: fetchElements()) {
	        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        ViewGroup haikuView = (ViewGroup) inflater.inflate(R.layout.haiku, null);
	        
	        ((TextView) haikuView.findViewById(R.id.haiku_text)).setText(h.getText());
	        
	        haikuList.addView(haikuView);
        }
    }
    
    abstract protected List<Haiku> fetchElements(); 
}