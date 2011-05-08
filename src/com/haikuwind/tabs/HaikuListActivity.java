package com.haikuwind.tabs;

import java.util.Calendar;
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
import android.widget.RelativeLayout;
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
    private static final int VALIDITY_PERIOD = 60; //data expires after one hour

    @SuppressWarnings("unused")
    private final static String TAG = HaikuListActivity.class.getSimpleName();

    private static final int ERROR_TRY_AGAIN_REFRESH = 0;

    protected boolean dataDirty = true;
    private boolean isForeground = false;
    
    protected Map<String, Haiku> haikuMap = new HashMap<String, Haiku>();
    protected Calendar lastUpdate;
    
    private HaikuController shareController = new ShareController(haikuMap, this);
    private HaikuController voteController = new VoteController(haikuMap, this);
    private HaikuController favoriteController = new FavoriteController(haikuMap, this);

    private ProgressDialog progressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.haiku_list);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataDirty || isDataObsolete()) {
            refreshData();
        }

        isForeground = true;

    }

    protected boolean isDataObsolete() {
        Calendar firstValidTime = Calendar.getInstance();
        firstValidTime.add(Calendar.MINUTE, -VALIDITY_PERIOD);
        return (firstValidTime.after(lastUpdate));
    }
    
    private void refreshData() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        new RefreshTask(progressDialog).start();
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
        if(progressDialog !=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
                        }
                    });
            break;

        default:
            return super.onCreateDialog(id);
        }
        return builder.create();
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
        dataDirty = true;
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
            
            ((RelativeLayout.LayoutParams) haikuView.findViewById(R.id.haiku_text).getLayoutParams()).
                    addRule(RelativeLayout.BELOW, R.id.haiku_star);
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

    protected List<Haiku> updateData() throws FeedException {
        List<Haiku> haikuResponse = fetchElements();
        haikuMap.clear();
        for(Haiku h: haikuResponse) {
            haikuMap.put(h.getId(), h);
        }
        
        //newer first
        Collections.sort(haikuResponse, new NewerFirstComparator());
        lastUpdate = Calendar.getInstance();
        
        return haikuResponse;
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
            dataDirty = false;
        }

        @Override
        protected void execute() throws Exception {
            haikuResponse = updateData();
        }
    }

}