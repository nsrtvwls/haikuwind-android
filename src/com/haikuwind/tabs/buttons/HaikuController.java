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
        v = getParentHaikuView(v);
        String haikuId = ((TextView) v.findViewById(R.id.haiku_id)).getText().toString();
        return haikuMap.get(haikuId);
    }
    
    /**
     * Find view with id 'haiku' in the upper view hierarchy
     * TODO check if there is standard method
     * @param v
     * @return
     */
    protected View getParentHaikuView(View v) {
        View rootView = v.getRootView();
        do {
            if(v.getId()!=R.id.haiku) {
                v = (View) v.getParent();
            } else {
                return v;
            }
        } while(v!=rootView);
        throw new IllegalStateException("root view for single haiku is not found"); 
    }
    
    public abstract void bind(View v);
}
