package com.haikuwind.tabs.buttons;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;

public class ShareController implements OnClickListener {
    @SuppressWarnings("unused")
    private static final String TAG = ShareController.class.getSimpleName();

    private Haiku haiku;
    
    /**we need application to start twitter, but this link may cause memory leak*/
    private WeakReference<Activity> app;

    private ShareController(Activity app, Haiku haiku) {
        this.haiku = haiku;
        this.app = new WeakReference<Activity>(app);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);

        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, app.get().getString(R.string.msg_title));
        i.putExtra(Intent.EXTRA_TEXT, haiku.getText());

        app.get().startActivity(Intent.createChooser(i, app.get().getString(R.string.post_using)));
    }

    public static void bind(View haikuView, Haiku haiku, Activity application) {
        View shareBtn = haikuView.findViewById(R.id.haiku_share);
        shareBtn.setVisibility(View.VISIBLE);

        shareBtn.setOnClickListener(new ShareController(application, haiku));
    }
}
