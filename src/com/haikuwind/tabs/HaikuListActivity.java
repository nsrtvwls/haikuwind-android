package com.haikuwind.tabs;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HaikuListData;
import com.haikuwind.feed.HaikuWindData;
import com.haikuwind.feed.fetch.FeedException;
import com.haikuwind.notification.DataUpdater;
import com.haikuwind.notification.TabUpdater;
import com.haikuwind.notification.UpdateAction;
import com.haikuwind.tabs.buttons.FavoriteController;
import com.haikuwind.tabs.buttons.HaikuController;
import com.haikuwind.tabs.buttons.HasFavoriteBtn;
import com.haikuwind.tabs.buttons.HasShareBtn;
import com.haikuwind.tabs.buttons.HasVoteBtn;
import com.haikuwind.tabs.buttons.ShareController;
import com.haikuwind.tabs.buttons.VoteController;

abstract public class HaikuListActivity extends Activity implements HasShareBtn {

    //view must be updated after data
    protected static final int DATA_UPDATE_PRIORITY = 1;
    protected static final int VIEW_UPDATE_PRIORITY = 0;

    @SuppressWarnings("unused")
    private final static String TAG = HaikuListActivity.class.getSimpleName();

    private static final int ERROR_TRY_AGAIN_REFRESH = 0;

    protected final HaikuListData data = HaikuWindData.getInstance().getHaikuListData(getClass());
    
    private HaikuController shareController;
    private HaikuController voteController;
    private HaikuController favoriteController;

    private ProgressDialog progressDialog;
    private ProgressTask progressTask;
    
    protected BroadcastReceiver dataRefresher = new DataUpdater(data);
    protected BroadcastReceiver viewRefresher = new TabUpdater(this);
    
    /**
     * date of the newest haiku.
     * since data is shared between two views (portrait/landscape),
     * we store last displayed haiku time to identify if data update was performed in other orientation 
     * and we need to update current view.
     */
    private Date lastDisplayedDate;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shareController = new ShareController(data, this);
        voteController = new VoteController(data, getUserId(), this);
        favoriteController = new FavoriteController(data, getUserId(), this);

        setContentView(R.layout.haiku_list);
        
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(UpdateAction.REFRESH.toString());
        filter.setPriority(DATA_UPDATE_PRIORITY);
        registerReceiver(dataRefresher, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        adjustView();

        IntentFilter filter = new IntentFilter();
        for(UpdateAction action: UpdateAction.values()) {
            filter.addAction(action.toString());
        }
        filter.setPriority(VIEW_UPDATE_PRIORITY);
        registerReceiver(viewRefresher, filter);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fly_in);
        findViewById(R.id.haiku_list).setAnimation(anim);
        anim.start();
    }

    public void adjustView() {
        if (data.isDataDirty()) {
            refreshData();
        } else if(lastDisplayedDate==null || data.isViewObsolete(lastDisplayedDate)) {
            renderNewHaiku(data.getHaikuList(), true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(progressDialog !=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        
        if(progressTask!=null && progressTask.isAlive()) {
            progressTask.interrupt();
        }

        unregisterReceiver(viewRefresher);
        
        //TODO doesn't appear
//        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.fly_in);
//        findViewById(R.id.haiku_list).setAnimation(anim);
//        anim.start();
   }
    
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(dataRefresher);
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

    private void refreshData() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        progressTask = new RefreshTask(progressDialog);
        progressTask.start();
    }

    
    private void renderNewHaiku(List<Haiku> haikuList, boolean erase) {
        LinearLayout haikuListView = (LinearLayout) findViewById(R.id.haiku_list);
        
        if(erase) {
            haikuListView.removeAllViews();
        }
        
        int i=0;
        for (Haiku h : haikuList) {
            ViewGroup haikuView = createSingleHaikuWidget(h);
            if(!erase) {
                haikuListView.addView(haikuView, i++);
            } else {
                haikuListView.addView(haikuView);
            }
        }
        
        if(haikuList.size()>0) {
            lastDisplayedDate = haikuList.get(0).getTime();
        }
    }
    
    protected ViewGroup createSingleHaikuWidget(Haiku h) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup haikuView = (ViewGroup) inflater
                .inflate(R.layout.haiku, null);

        ((TextView) haikuView.findViewById(R.id.haiku_id)).setText(h.getId());
        ((TextView) haikuView.findViewById(R.id.haiku_text)).setText(h.getText());
        
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
    
    protected String getUserId() {
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }

    abstract protected List<Haiku> fetchElements() throws FeedException;
    
    protected boolean eraseOldHaikus() {
        return true;
    }
    

    private class RefreshTask extends ProgressTask {
        List<Haiku> lastUpdateResult;
        
        public RefreshTask(ProgressDialog progressDialog) {
            super(progressDialog);
        }

        @Override
        protected void handleError() {
            onCreateDialog(ERROR_TRY_AGAIN_REFRESH).show();
        }

        @Override
        protected void handleSuccess() {
            synchronized (data) {
                boolean erase = eraseOldHaikus(); //for timeline changes after data update
                data.updateHaikuList(lastUpdateResult, erase);
                data.setDataDirty(false);
                renderNewHaiku(lastUpdateResult, erase);
            }
        }

        @Override
        protected void execute() throws Exception {
            lastUpdateResult = fetchElements();
        }
    }

}