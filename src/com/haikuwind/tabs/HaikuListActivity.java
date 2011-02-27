package com.haikuwind.tabs;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.haikuwind.R;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.menu.dialogs.DialogBuilder;

abstract class HaikuListActivity extends Activity {
    @SuppressWarnings("unused")
    private final static String TAG = HaikuListActivity.class.getSimpleName();
    
    protected final boolean voteEnabled;

    public HaikuListActivity(boolean voteEnabled) {
        this.voteEnabled = voteEnabled;
    }

    protected boolean dataObsolete = true;
    
    private DialogBuilder dialogBuilder = new DialogBuilder(this);
    
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
        if(DialogBuilder.ERROR_TRY_AGAIN_REFRESH==id) {
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                
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
            showDialog(DialogBuilder.ERROR);
            return;
        }

        haikuListView.removeAllViews();
        for (Haiku h : haikuResponse) {
            ViewGroup haikuView = createSingleHaikuWidget(h);

            haikuListView.addView(haikuView);
            
        }
        
        dataObsolete = false;
    }

    protected ViewGroup createSingleHaikuWidget(Haiku h) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup haikuView = (ViewGroup) inflater.inflate(R.layout.haiku, null);

        ((TextView) haikuView.findViewById(R.id.haiku_text)).setText(h.getText());
        ((TextView) haikuView.findViewById(R.id.haiku_points)).setText(Integer.toString(h.getPoints()));
        
        if(!voteEnabled) {
            ((View) haikuView.findViewById(R.id.thumb_up)).setVisibility(View.INVISIBLE);
            ((View) haikuView.findViewById(R.id.thumb_down)).setVisibility(View.INVISIBLE);
        }
        
        View favoriteToggle = haikuView.findViewById(R.id.haiku_favorite);
        favoriteToggle.setOnClickListener(new Marker(h));
        updateToggleFavorite(favoriteToggle, h);
        
        return haikuView;
    }

    abstract protected List<Haiku> fetchElements() throws FeedException;
    
    private void updateToggleFavorite(View toggle, Haiku h) {
        ((ToggleButton) toggle).setChecked(h.isFavoritedByMe());
    }
    
    private class Marker implements OnClickListener {
        
        private Haiku haiku;

        public Marker(Haiku haiku) {
            this.haiku = haiku;
        }

        @Override
        public void onClick(View v) {
            ToggleButton toggle = (ToggleButton) v;
            if(toggle.isChecked()) {
                try {
                    HttpRequest.favorite(haiku.getId());
                    haiku.setFavoritedByMe(true);
                } catch (FeedException e) {
                    Log.e(TAG, "error while marking favorite", e);
                    updateToggleFavorite(toggle, haiku);
                }
            }
            
        }
        
    }
}