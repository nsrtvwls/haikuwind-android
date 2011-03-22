package com.haikuwind.tabs;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.dialogs.CancelListener;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateListener;
import com.haikuwind.tabs.buttons.FavoriteController;
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
                        }
                    });
            break;

        default:
            return super.onCreateDialog(id);
        }
        return builder.create();
    }

    protected void refreshData() {
        LinearLayout haikuListView = (LinearLayout) findViewById(R.id.haiku_list);

        List<Haiku> haikuResponse;
        try {
            haikuResponse = fetchElements();
        } catch (FeedException e) {
            // TODO how to show the same dialog that is open now?
            onCreateDialog(ERROR_TRY_AGAIN_REFRESH).show();
            return;
        }

        haikuListView.removeAllViews();
        for (Haiku h : haikuResponse) {
            ViewGroup haikuView = createSingleHaikuWidget(h);

            haikuListView.addView(haikuView);

        }

        dataObsolete = false;
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

        ((TextView) haikuView.findViewById(R.id.haiku_text)).setText(h
                .getText());
        ((TextView) haikuView.findViewById(R.id.haiku_points)).setText(Integer
                .toString(h.getPoints()));

        if (this instanceof HasVoteBtn) {
            VoteController.bind(getApplicationContext(), haikuView, h);
        }
        if(this instanceof HasFavoriteBtn) {
            FavoriteController.bind(haikuView, h, getApplicationContext());
        }
        if(this instanceof HasShareBtn) {
            ShareController.bind(haikuView, h, this);
        }

        int userImg = h.getUserRank().getSmallImageId();
        ((ImageView) haikuView.findViewById(R.id.haiku_author_image))
                .setImageResource(userImg);

        return haikuView;
    }

    abstract protected List<Haiku> fetchElements() throws FeedException;

}