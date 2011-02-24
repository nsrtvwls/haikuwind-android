package com.haikuwind.tabs;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.menu.dialogs.DialogBuilder;

public abstract class HaikuListActivity extends Activity {
    @SuppressWarnings("unused")
    private final static String TAG = HaikuListActivity.class.getName();
    
    protected final boolean voteEnabled;
    protected boolean dataObsolete = true;
    
    private DialogBuilder dialogBuilder = new DialogBuilder(this);
    
    protected HaikuListActivity(boolean voteEnabled) {
        this.voteEnabled = voteEnabled;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.haiku_list);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        //if update isn't needed or application hasn't been initialized correctly yet, then skip
        if(!dataObsolete) {
            return;
        }

        refreshData();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = dialogBuilder.buildDialog(id);
        if(DialogBuilder.ERROR_TRY_AGAIN==id) {
            builder.setPositiveButton("Try again", new OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refreshData();
                }
            });
        }
        return builder.create();
    }
    
    private void refreshData() {
        LinearLayout haikuListView = (LinearLayout) findViewById(R.id.haiku_list);
        
        List<Haiku> haikuResponse;
        try {
            haikuResponse = fetchElements();
        } catch(FeedException e) {
            showDialog(DialogBuilder.ERROR_TRY_AGAIN);
            return;
        }

        haikuListView.removeAllViews();
        for (Haiku h : haikuResponse) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup haikuView = (ViewGroup) inflater.inflate(R.layout.haiku,
                    null);

            ((TextView) haikuView.findViewById(R.id.haiku_text)).setText(h.getText());
            ((TextView) haikuView.findViewById(R.id.haiku_points)).setText(Integer.toString(h.getPoints()));
            
            if(!voteEnabled) {
                ((View) haikuView.findViewById(R.id.thumb_up)).setVisibility(View.INVISIBLE);
                ((View) haikuView.findViewById(R.id.thumb_down)).setVisibility(View.INVISIBLE);
            }

            haikuListView.addView(haikuView);
            
        }
        
        dataObsolete = false;
    }

    abstract protected List<Haiku> fetchElements() throws FeedException;
}