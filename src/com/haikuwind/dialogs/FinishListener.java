package com.haikuwind.dialogs;

import android.app.Activity;
import android.content.DialogInterface;

public class FinishListener implements DialogInterface.OnClickListener {
    Activity activity;
    
    public FinishListener(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        activity.finish();
    }
}
