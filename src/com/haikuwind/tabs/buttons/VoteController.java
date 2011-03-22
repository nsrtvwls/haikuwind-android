package com.haikuwind.tabs.buttons;

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

public class VoteController implements View.OnClickListener {
        private static final String TAG = VoteController.class.getSimpleName();
        
        private final Haiku haiku;
        private final View[] buttons;
        private final Context context;
        
        private VoteController(Context context, Haiku haiku, View... buttons) {
            this.context = context;
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
                updateVoteButtons(haiku, buttons);
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
        private static void updateVoteButtons(Haiku h, View... buttons) {
            boolean enabled = h.getTimesVotedByMe()<UserInfo.getCurrent().getRank().getPower();
            for(View btn: buttons) {
                btn.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
            }
        }
        
        public static void bind(Context context, View haikuView, Haiku haiku) {
            View thumbUp = ((View) haikuView.findViewById(R.id.thumb_up));
            View thumbDown = ((View) haikuView.findViewById(R.id.thumb_down));
            
            VoteController voter = new VoteController(context, haiku, thumbUp, thumbDown);
            thumbUp.setOnClickListener(voter);
            thumbDown.setOnClickListener(voter);
            
            updateVoteButtons(haiku, thumbUp, thumbDown);
        }

}
