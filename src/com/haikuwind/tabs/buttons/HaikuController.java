package com.haikuwind.tabs.buttons;

import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;

public abstract class HaikuController implements OnClickListener {
    private Map<String, Haiku> haikuMap;

    public HaikuController(Map<String, Haiku> haikuMap) {
        this.haikuMap = haikuMap;
    }

    protected Haiku getHaiku(View v) {
        String haikuId = ((TextView) v.findViewById(R.id.haiku_id)).getText().toString();
        return haikuMap.get(haikuId);
    }
    
    public abstract void bind(View v);
}
