package com.bbr.hidei.view.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.bbr.hidei.heidi.R;


public class SplashActivity extends Activity {

    public static boolean ACTIVITY_INITIALIZED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();       // 2초짜리
            }
        }, 2000);
    }
}
