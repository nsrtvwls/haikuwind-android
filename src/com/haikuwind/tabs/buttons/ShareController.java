package com.haikuwind.tabs.buttons;

import java.lang.ref.WeakReference;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;

public class ShareController extends HaikuController {
    @SuppressWarnings("unused")
    private static final String TAG = ShareController.class.getSimpleName();

    /**we need application to start twitter, but this link may cause memory leak*/
    private WeakReference<Activity> app;

    public ShareController(Map<String, Haiku> haikuMap, Activity app) {
        super(haikuMap);
        this.app = new WeakReference<Activity>(app);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);

        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, app.get().getString(R.string.msg_title));
        i.putExtra(Intent.EXTRA_TEXT, getHaiku(v).getText());

        app.get().startActivity(Intent.createChooser(i, app.get().getString(R.string.post_using)));
    }

    @Override
    public void bind(View haikuView) {
        View shareBtn = haikuView.findViewById(R.id.haiku_share);
        shareBtn.setVisibility(View.VISIBLE);

        shareBtn.setOnClickListener(this);
    }
}