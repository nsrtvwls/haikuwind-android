package com.haikuwind.tabs;

import android.app.Activity;
import android.os.Bundle;

import com.haikuwind.R;

public class Timeline extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.haiku_list);
    }
}