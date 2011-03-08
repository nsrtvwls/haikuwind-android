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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.dialogs.CancelListener;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;

abstract class HaikuListActivity extends Activity {
    @SuppressWarnings("unused")
    private final static String TAG = HaikuListActivity.class.getSimpleName();

    private static final int ERROR_TRY_AGAIN_REFRESH = 0;
    protected static final int ERROR_CANCEL = 1;
    
    protected final boolean voteEnabled;

    public HaikuListActivity(boolean voteEnabled) {
        this.voteEnabled = voteEnabled;
    }

    protected boolean dataObsolete = true;
    
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setNegativeButton(R.string.cancel, new CancelListener())
                .setMessage(R.string.oops);
        
        switch(id) {
        case ERROR_TRY_AGAIN_REFRESH:
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refreshData();
                }
            });
            break;
        
        case ERROR_CANCEL:
            break;
        
        default:
            return super.onCreateDialog(id);
        }
        return builder.create();
    }
    
    private void refreshData() {
        LinearLayout haikuListView = (LinearLayout) findViewById(R.id.haiku_list);
        
        List<Haiku> haikuResponse;
        try {
            haikuResponse = fetchElements();
        } catch(FeedException e) {
            showDialog(ERROR_TRY_AGAIN_REFRESH);
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
        
        int userImg = h.getUserRank().getSmallImageId(); 
        ((ImageView) haikuView.findViewById(R.id.haiku_author_image)).setImageResource(userImg);
        
        return haikuView;
    }

    abstract protected List<Haiku> fetchElements() throws FeedException;
    
    private void updateToggleFavorite(View toggle, Haiku h) {
        if(h.isFavoritedByMe()) {
            toggle.setBackgroundResource(R.drawable.toggle_favorite_checked);
        } else {
            toggle.setBackgroundResource(R.drawable.toggle_favorite_unchecked);
        }
    }
    
    private class Marker implements OnClickListener {
        private Haiku haiku;

        public Marker(Haiku haiku) {
            this.haiku = haiku;
        }

        @Override
        public void onClick(View v) {
            if(!haiku.isFavoritedByMe()) {
                try {
                    HttpRequest.favorite(haiku.getId());
                    haiku.setFavoritedByMe(true);
                } catch (FeedException e) {
                    Log.e(TAG, "error while marking favorite", e);
                    showDialog(ERROR_CANCEL);
                } finally {
                    updateToggleFavorite(v, haiku);
                }
            }
            
        }
        
    }
}