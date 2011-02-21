package com.haikuwind.tabs;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.UserInfoHolder;
import com.haikuwind.feed.Haiku;

public abstract class HaikuListActivity extends Activity {
    @SuppressWarnings("unused")
    private final static String TAG = HaikuListActivity.class.getName();
    
    protected final boolean voteEnabled;
    
    protected HaikuListActivity(boolean voteEnabled) {
        this.voteEnabled = voteEnabled;
    }

    protected String getUserId() {
        return UserInfoHolder.getUserId();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.haiku_list);

        LinearLayout haikuList = (LinearLayout) findViewById(R.id.haiku_list);

        for (Haiku h : fetchElements()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup haikuView = (ViewGroup) inflater.inflate(R.layout.haiku,
                    null);

            ((TextView) haikuView.findViewById(R.id.haiku_text)).setText(h.getText());
            ((TextView) haikuView.findViewById(R.id.haiku_points)).setText(Integer.toString(h.getPoints()));
            
            if(!voteEnabled) {
                ((View) haikuView.findViewById(R.id.thumb_up)).setVisibility(View.INVISIBLE);
                ((View) haikuView.findViewById(R.id.thumb_down)).setVisibility(View.INVISIBLE);
            }

            haikuList.addView(haikuView);
        }
    }

    abstract protected List<Haiku> fetchElements();
}