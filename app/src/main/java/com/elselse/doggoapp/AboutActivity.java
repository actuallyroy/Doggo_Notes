package com.elselse.doggoapp;


import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class AboutActivity extends Activity {
    MediaPlayer mediaPlayer;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView doggoImg = findViewById(R.id.doggoImg);
        TextView versionText = findViewById(R.id.versionTxt);
        versionText.setText("Version " + BuildConfig.VERSION_NAME);

        ScrollView scrollview = findViewById(R.id.scrollView2);
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int i = scrollview.getScrollY();
                scrollview.smoothScrollBy(0, 1);
                Log.d("Scroll", String.valueOf(scrollview.getScrollY()));
                Log.d("I", String.valueOf(i));

                if(i != scrollview.getScrollY() || i == 0)
                    h.postDelayed(this, 50);
                else
                    h.removeCallbacks(this);
            }
        };
        h.postDelayed(r, 50);

        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                h.removeCallbacks(r);
                return false;
            }
        });

        doggoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) Math.round(Math.random()*4);
                Log.d("i", String.valueOf(i));
                if(mediaPlayer!= null)
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                switch (i) {
                    case 0:
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dogbark_1);
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dogbark_2);
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dogbark_3);
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dogbark_4);
                        break;
                    case 4:
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dogbark_5);
                        break;
                }
                mediaPlayer.start();
            }
        });
    }
}