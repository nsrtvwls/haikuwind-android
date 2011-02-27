package com.haikuwind.menu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.feed.FeedException;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.feed.UserInfo;

public class DialogBuilder {
    public static final int POST_HAIKU = 0;
    public static final int USER_INFO = 1;
    public static final int SUGGEST_NETWORK_SETTINGS = 2;
    public static final int ERROR = 3;
    public static final int ERROR_TRY_AGAIN_POST_HAIKU = 4;
    public static final int ERROR_TRY_AGAIN_REFRESH = 5;
    
    private static final String TAG = DialogBuilder.class.getName();

    private Activity activity;

    public DialogBuilder(Activity context) {
        this.activity = context;
    }

    public AlertDialog.Builder buildDialog(int id) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View layout = null;
        switch (id) {
        case POST_HAIKU:
            layout = buildPostHaikuDialog(inflater, builder);
            break;
            
        case USER_INFO:
            layout = buildUserInfoDialog(inflater, builder);
            break;
            
        case SUGGEST_NETWORK_SETTINGS:
            return suggestNetworkSettingsDialog(builder);
            
        case ERROR:
            return buildErrorTryAgainDialog(builder);
            
        case ERROR_TRY_AGAIN_POST_HAIKU:
            return buildErrorTryAgainDialog(builder).setPositiveButton(R.string.try_again, new OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.showDialog(POST_HAIKU);
                }
            });

        default:
            // TODO
            Log.e(TAG, "incorrect dialog id: " + id);
            layout = new View(activity);
        }

        builder.setView(layout);
        return builder;
    }

    private AlertDialog.Builder buildErrorTryAgainDialog(Builder builder) {
        return builder.setMessage(R.string.oops)
            .setCancelable(true)
            .setNegativeButton(R.string.cancel, new CancelListener());
    }

    public View buildPostHaikuDialog(LayoutInflater inflater,
            AlertDialog.Builder builder) {
        View layout = inflater.inflate(R.layout.post_haiku_dialog, null);

        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        doPostHaiku(dialog);
                    }
                });

        builder.setNegativeButton(R.string.cancel, new CancelListener());
        return layout;
    }
    
    private void doPostHaiku(DialogInterface dialog) {
        View haikuTextView = ((Dialog) dialog).findViewById(R.id.haiku_text);
        CharSequence haiku = ((TextView) haikuTextView).getText();
        
        try {
            HttpRequest.newHaiku(haiku);
            ((TextView) haikuTextView).setText("");
        } catch(FeedException e) {
            activity.showDialog(ERROR_TRY_AGAIN_POST_HAIKU);
        }
    }

    private View buildUserInfoDialog(LayoutInflater inflater, Builder builder) {
        View layout = inflater.inflate(R.layout.user_info_dialog, null);

        UserInfo user = UserInfo.getCurrent();
        
        ((TextView) layout.findViewById(R.id.user_rank)).setText(user.getRank()
                .getRankStringId());
        String value = Integer.toString(user.getRank().getPower());
        ((TextView) layout.findViewById(R.id.user_voting_power)).setText(value);
        value = Integer.toString(user.getScore());
        ((TextView) layout.findViewById(R.id.user_score)).setText(value);
        value = Integer.toString(user.getFavoritedTimes()) + " "
                + activity.getString(R.string.times);
        ((TextView) layout.findViewById(R.id.user_favorited_times))
                .setText(value);

        builder.setNegativeButton(R.string.close, new CancelListener());
        return layout;
    }
    
    private AlertDialog.Builder suggestNetworkSettingsDialog(AlertDialog.Builder builder) {
         return builder.setTitle(R.string.connection_failed)
                .setMessage(R.string.suggest_network_settings)
                .setPositiveButton(R.string.accept,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                // THIS IS WHAT YOU ARE DOING, Jul
                                activity.startActivity(new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                activity.finish();
                            }
                        });
    }

    public static class CancelListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog,
                int which) {
            dialog.cancel();
        }
    }
}
