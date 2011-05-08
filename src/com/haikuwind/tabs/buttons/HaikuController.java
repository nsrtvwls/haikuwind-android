package com.haikuwind.tabs.buttons;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HaikuListData;

public abstract class HaikuController implements OnClickListener {
    private HaikuListData haikuListData;

    public HaikuController(HaikuListData haikuListData) {
        this.haikuListData = haikuListData;
    }

    protected Haiku getHaiku(View v) {
        v = getParentHaikuView(v);
        String haikuId = ((TextView) v.findViewById(R.id.haiku_id)).getText().toString();
        return haikuListData.getHaikuMap().get(haikuId);
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
