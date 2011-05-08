package com.haikuwind.tabs.buttons;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.haikuwind.R;
import com.haikuwind.feed.HaikuListData;

public class ShareController extends HaikuController {
    @SuppressWarnings("unused")
    private static final String TAG = ShareController.class.getSimpleName();

    private Activity app;

    public ShareController(HaikuListData haikuListData, Activity app) {
        super(haikuListData);
        this.app = app;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);

        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, app.getString(R.string.msg_title));
        i.putExtra(Intent.EXTRA_TEXT, getHaiku(v).getText());

        app.startActivity(Intent.createChooser(i, app.getString(R.string.post_using)));
    }

    @Override
    public void bind(View haikuView) {
        View shareBtn = haikuView.findViewById(R.id.haiku_share);
        shareBtn.setVisibility(View.VISIBLE);

        shareBtn.setOnClickListener(this);
    }
}
