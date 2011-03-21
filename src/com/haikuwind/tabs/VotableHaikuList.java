package com.haikuwind.tabs;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.haikuwind.R;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.feed.UserInfo;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;

abstract class VotableHaikuList extends HaikuListActivity {
    private final static String TAG = VotableHaikuList.class.getSimpleName();
    
    protected VotableHaikuList() {
        super(true);
    }

    @Override
    protected ViewGroup createSingleHaikuWidget(Haiku h) {
        ViewGroup haikuView = super.createSingleHaikuWidget(h);
        
        View thumbUp = ((View) haikuView.findViewById(R.id.thumb_up));
        View thumbDown = ((View) haikuView.findViewById(R.id.thumb_down));
        
        Voter voter = new Voter(h, thumbUp, thumbDown);
        thumbUp.setOnClickListener(voter);
        thumbDown.setOnClickListener(voter);
        
        updateVoteButtons(h, thumbUp, thumbDown);
        
        return haikuView;
    }


    /**
     * set buttons enabled if current user has enough power
     */
    private void updateVoteButtons(Haiku h, View... buttons) {
        boolean enabled = h.getTimesVotedByMe()<UserInfo.getCurrent().getRank().getPower();
        for(View btn: buttons) {
            btn.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * TODO: it's not optimized to create new listener for each button
     * The class is inner to get use of Activity.showDialog(DialogBuilder.ERROR)
     * @author oakjumper
     *
     */
    private class Voter implements View.OnClickListener {
        private final Haiku haiku;
        private final View[] buttons;
        
        public Voter(Haiku haiku, View... buttons) {
            this.haiku = haiku;
            this.buttons = buttons;
        }

        @Override
        public void onClick(View v) {
            for(View btn: buttons) {
                btn.setVisibility(View.INVISIBLE);
            }
            
            final View haikuView = (View) v.getParent().getParent();
            
            boolean isGood = v.getId()==R.id.thumb_up;
            
            try {
                HttpRequest.vote(haiku.getId(), isGood);
                
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.spin);
                haikuView.setAnimation(anim);
                anim.start();
                
                //TODO: ask catpad, maybe count only vote abs value?
                haiku.setTimesVotedByMe(haiku.getTimesVotedByMe()+1);
                haiku.setPoints(haiku.getPoints()+(isGood? 1 : -1));
                
                TextView points = (TextView) ((View) v.getParent()).findViewById(R.id.haiku_points);
                points.setText(Integer.toString(haiku.getPoints()));

                UpdateNotifier.fireUpdate(Update.VOTE, haiku);
                
            } catch (FeedException e) {
                Log.e(TAG, "error in vote", e);                
                Toast.makeText(getApplicationContext(), R.string.toast_error_try_again, Toast.LENGTH_SHORT);
            } finally {
                updateVoteButtons(haiku, buttons);
            }
            
            if(haiku.getPoints() <= Haiku.MIN_POINTS) {
                hideHaiku(haikuView);
            }
        }

        private void hideHaiku(final View haikuView) {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear);
            haikuView.setAnimation(anim);
            anim.start();
            
            anim.setAnimationListener(new AnimationListener() {
                
                @Override
                public void onAnimationStart(Animation animation) {
                    
                }
                
                @Override
                public void onAnimationRepeat(Animation animation) {
                    
                }
                
                @Override
                public void onAnimationEnd(Animation animation) {
                    haikuView.setVisibility(View.GONE);
                }
            });
        }
    }

}