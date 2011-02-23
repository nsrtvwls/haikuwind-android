package com.haikuwind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreen.this, HaikuWind.class));
                finish();
            }
        }).start();
    }

}
