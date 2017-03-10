package com.example.bnariv2;


import android.app.Activity;
import android.os.*;
import android.content.*;
import android.view.*;

public class IntroActivity extends Activity {

    Handler h;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);
        h = new Handler(); 
        h.postDelayed(mrun, 1500);
}
    Runnable mrun = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }
}
