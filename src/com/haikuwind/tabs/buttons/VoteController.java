package com.haikuwind.tabs.buttons;

import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.feed.UserInfo;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;

public class VoteController extends HaikuController {
        private static final String TAG = VoteController.class.getSimpleName();
        
        private final Context context;
        
        public VoteController(Map<String, Haiku> haikuMap, Context context) {
            super(haikuMap);
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            setThumbsVisibility((View) v.getParent(), View.INVISIBLE);
            
            final View haikuView = (View) v.getParent().getParent();
            
            boolean isGood = v.getId()==R.id.thumb_up;
            
            Haiku haiku = getHaiku(v);
            try {
                HttpRequest.vote(haiku.getId(), isGood);
                
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.spin);
                haikuView.setAnimation(anim);
                anim.start();
                
                //TODO: ask catpad, maybe count only vote abs value?
                haiku.setTimesVotedByMe(haiku.getTimesVotedByMe()+1);
                haiku.setPoints(haiku.getPoints()+(isGood? 1 : -1));
                
                TextView points = (TextView) ((View) v.getParent()).findViewById(R.id.haiku_points);
                points.setText(Integer.toString(haiku.getPoints()));

                UpdateNotifier.fireUpdate(Update.VOTE, haiku);
                
            } catch (Exception e) {
                Log.e(TAG, "error in vote", e);                
                Toast.makeText(context, R.string.toast_error_try_again, Toast.LENGTH_SHORT);
            } finally {
                updateVoteButtons(haikuView);
            }
            
            if(haiku.getPoints() <= Haiku.MIN_POINTS) {
                hideHaiku(haikuView);
            }
        }

        private void hideHaiku(final View haikuView) {
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.disappear);
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
        
        /**
         * set buttons enabled if current user has enough power
         */
        private void updateVoteButtons(View haikuView) {
            Haiku h = getHaiku(haikuView);
            boolean enabled = h.getTimesVotedByMe()<UserInfo.getCurrent().getRank().getPower();
            setThumbsVisibility(haikuView, enabled ? View.VISIBLE : View.INVISIBLE);
        }

        private void setThumbsVisibility(View parent, int visibility) {
            getThumbUp(parent).setVisibility(visibility);
            getThumbDown(parent).setVisibility(visibility);
        }
        
        @Override
        public void bind(View haikuView) {
            getThumbUp(haikuView).setOnClickListener(this);
            getThumbDown(haikuView).setOnClickListener(this);
            
            updateVoteButtons(haikuView);
        }
        
        private View getThumbUp(View parent) {
            return parent.findViewById(R.id.thumb_up);
        }
        
        private View getThumbDown(View parent) {
            return parent.findViewById(R.id.thumb_down);
        }

}