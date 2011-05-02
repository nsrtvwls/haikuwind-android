package com.haikuwind.tabs;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.dialogs.CancelListener;
import com.haikuwind.dialogs.ProgressTask;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.NewerFirstComparator;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateListener;
import com.haikuwind.tabs.buttons.FavoriteController;
import com.haikuwind.tabs.buttons.HaikuController;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasShareBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;
import com.haikuwind.tabs.buttons.ShareController;
import com.haikuwind.tabs.buttons.VoteController;

abstract class HaikuListActivity extends Activity implements UpdateListener, HasShareBtn {
    @SuppressWarnings("unused")
    private final static String TAG = HaikuListActivity.class.getSimpleName();

    private static final int ERROR_TRY_AGAIN_REFRESH = 0;

    protected boolean dataObsolete = true;
    private boolean isForeground = false;
    
    protected Map<String, Haiku> haikuMap = new HashMap<String, Haiku>();
    
    private HaikuController shareController = new ShareController(haikuMap, this);
    private HaikuController voteController = new VoteController(haikuMap, this);
    private HaikuController favoriteController = new FavoriteController(haikuMap, this);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.haiku_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if update isn't needed or application hasn't been initialized
        // correctly yet, then skip
        if (dataObsolete) {
            refreshData();
        }

        isForeground = true;

    }

    private void refreshData() {
        ProgressDialog pd = ProgressDialog.show(this, "", 
                getString(R.string.loading));
        new RefreshTask(pd).start();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fly_in);
        findViewById(R.id.haiku_list).setAnimation(anim);
        anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        isForeground = false;
    }

    protected boolean isForeground() {
        return isForeground;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setNegativeButton(R.string.cancel, new CancelListener())
                .setMessage(R.string.oops);

        switch (id) {
        case ERROR_TRY_AGAIN_REFRESH:
            builder.setPositiveButton("Try again",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            refreshData();
                            dialog.dismiss();
                        }
                    });
            break;

        default:
            return super.onCreateDialog(id);
        }
        return builder.create();
    }

    
    protected void updateStored(List<Haiku> haikuResponse) {
        haikuMap.clear();
        for(Haiku h: haikuResponse) {
            haikuMap.put(h.getId(), h);
        }
    }

    protected void renderNewHaiku(List<Haiku> haikuResponse) {
        LinearLayout haikuListView = (LinearLayout) findViewById(R.id.haiku_list);
        haikuListView.removeAllViews();
        for (Haiku h : haikuResponse) {
            ViewGroup haikuView = createSingleHaikuWidget(h);

            haikuListView.addView(haikuView);

        }
    }
    
    @Override
    public void processUpdate(Update update, Haiku haiku) {
        dataObsolete = true;
        if (isForeground()) {
            refreshData();
        }
    }

    protected ViewGroup createSingleHaikuWidget(Haiku h) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup haikuView = (ViewGroup) inflater
                .inflate(R.layout.haiku, null);

        ((TextView) haikuView.findViewById(R.id.haiku_id)).setText(h.getId());
        ((TextView) haikuView.findViewById(R.id.haiku_text)).setText(h
                .getText());
        
        int points = h.getPoints();
        TextView pointsView = ((TextView) haikuView.findViewById(R.id.haiku_points));
        if(points < Haiku.HALL_OF_FAME_POINTS) {
            pointsView.setText(Integer.toString(points));
        } else {
            pointsView.setVisibility(View.GONE);
            haikuView.findViewById(R.id.haiku_star).setVisibility(View.VISIBLE);
        }

        if (this instanceof HasVoteBtn) {
            voteController.bind(haikuView);
        }
        if(this instanceof HasFavoriteBtn) {
            favoriteController.bind(haikuView);
        }
        if(this instanceof HasShareBtn) {
            shareController.bind(haikuView);
        }

        int userImg = h.getUserRank().getSmallImageId();
        ((ImageView) haikuView.findViewById(R.id.haiku_author_image))
                .setImageResource(userImg);

        return haikuView;
    }

    abstract protected List<Haiku> fetchElements() throws FeedException;
    
    private class RefreshTask extends ProgressTask {
        public RefreshTask(ProgressDialog progressDialog) {
            super(progressDialog);
        }

        private List<Haiku> haikuResponse;

        @Override
        protected void handleError() {
            onCreateDialog(ERROR_TRY_AGAIN_REFRESH).show();
        }

        @Override
        protected void handleSuccess() {
            renderNewHaiku(haikuResponse);
            dataObsolete = false;
        }

        @Override
        protected void execute() throws Exception {
            haikuResponse = fetchElements();
            updateStored(haikuResponse);
            
            //newer first
            Collections.sort(haikuResponse, new NewerFirstComparator());
        }
    }

}