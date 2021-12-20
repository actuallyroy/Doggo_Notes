package com.elselse.doggoapp;


import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class AboutActivity extends Activity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView doggoImg = findViewById(R.id.doggoImg);
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