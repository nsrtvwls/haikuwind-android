package com.haikuwind.menu.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.UserInfoHolder;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.feed.UserInfo;

public class DialogBuilder {
    public static final int POST_HAIKU = 0;
    public static final int USER_INFO = 1;
    private static final String TAG = DialogBuilder.class.getName();

    private Context context;

    public DialogBuilder(Context context) {
        this.context = context;
    }

    public Dialog createDialog(int id) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layout = null;
        switch (id) {
        case POST_HAIKU:
            layout = buildPostHaikuDialog(inflater, builder);
            break;
        case USER_INFO:
            layout = buildUserInfoDialog(inflater, builder);
            break;
        default:
            // TODO
            Log.e(TAG, "incorrect dialog id: " + id);
            layout = new View(context);
        }

        builder.setView(layout);
        return builder.create();
    }

    private View buildPostHaikuDialog(LayoutInflater inflater,
            AlertDialog.Builder builder) {
        View layout = inflater.inflate(R.layout.post_haiku_dialog, null);

        builder.setPositiveButton(R.string.send,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (DialogInterface.BUTTON_POSITIVE == id) {
                            View haikuTextView = ((Dialog) dialog)
                                    .findViewById(R.id.haiku_text);
                            CharSequence haiku = ((TextView) haikuTextView)
                                    .getText();
                            HttpRequest.newHaiku(UserInfoHolder.getUserId(),
                                    haiku);
                        }
                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return layout;
    }

    private View buildUserInfoDialog(LayoutInflater inflater, Builder builder) {
        View layout = inflater.inflate(R.layout.user_info_dialog, null);

        UserInfo user = UserInfoHolder.getUserInfo();
        
        ((TextView) layout.findViewById(R.id.user_rank)).setText(user.getRank()
                .getRankStringId());
        String value = Integer.toString(user.getRank().getPower());
        ((TextView) layout.findViewById(R.id.user_voting_power)).setText(value);
        value = Integer.toString(user.getScore());
        ((TextView) layout.findViewById(R.id.user_score)).setText(value);
        value = Integer.toString(user.getFavoritedTimes()) + " "
                + context.getString(R.string.times);
        ((TextView) layout.findViewById(R.id.user_favorited_times))
                .setText(value);

        builder.setNegativeButton(R.string.close,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return layout;
    }

}
