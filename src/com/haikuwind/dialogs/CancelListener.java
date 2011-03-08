package com.haikuwind.dialogs;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class CancelListener implements OnClickListener {
    @Override
    public void onClick(DialogInterface dialog,
            int which) {
        dialog.cancel();
    }
}