package com.elselse.doggoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends Activity {

    int Red, Green, Blue;
    LinearLayout profileMenu;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = this.getWindow();
        window.setStatusBarColor(Color.GRAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("Accounts", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        profileMenu = findViewById(R.id.profile_menu);
        TextView welcomeTxt = findViewById(R.id.wlcmTxt),
                userNameTxt = findViewById(R.id.userNameTxt),
                aboutBtn = findViewById(R.id.aboutBtn);
        LinearLayout addNote = findViewById(R.id.addNote);
        FloatingActionButton fab = findViewById(R.id.fab);
        Button doneBtn = findViewById(R.id.doneBtn),
                cancelBtn = findViewById(R.id.cancel_button);


        String userName = prefs.getString("Logged", "");
        String name = prefs.getString(userName+"name","");
        welcomeTxt.setText(name);
        userNameTxt.setText(name.substring(0,1));
        String colorString = prefs.getString(userName+"Color","000000000");
        Log.d("Colors", colorString);
        Red = Integer.parseInt(colorString.substring(0,3));
        Green = Integer.parseInt(colorString.substring(3,6));
        Blue = Integer.parseInt(colorString.substring(6,9));
        if(Red == 0 || Green == 0 || Blue == 0){
            generateRandomColor();
            prefsEditor.putString(userName+"Color", String.format("%03d", Red)+String.format("%03d", Green)+String.format("%03d", Blue));
            prefsEditor.apply();
        }
        Log.d("Red", String.valueOf(Red));
        Log.d("Blue", String.valueOf(Blue));
        Log.d("Green", String.valueOf(Green));
        userNameTxt.setTextColor(Color.rgb(Red, Green, Blue));
        ConstraintLayout constraintLayout = findViewById(R.id.cL);
        ImageButton logoutButton = findViewById(R.id.logoutBtn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote.setVisibility(View.VISIBLE);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getDrawable(R.drawable.ic_done_icon);
                drawable.setColorFilter(Color.rgb(1,219,197), PorterDuff.Mode.MULTIPLY);
                view.setBackground(drawable);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawable.clearColorFilter();
                        //addNote.setVisibility(View.INVISIBLE);
                    }
                },200);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getDrawable(R.drawable.ic_cancel_icon);
                drawable.setColorFilter(Color.rgb(1,219,197), PorterDuff.Mode.MULTIPLY);
                view.setBackground(drawable);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawable.clearColorFilter();
                        addNote.setVisibility(View.INVISIBLE);
                    }
                },200);
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profileMenu.getVisibility() == View.INVISIBLE) {
                    showProfileMenu();
                }else{
                    hideProfileMenu();
                }
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefsEditor.remove("Logged");
                prefsEditor.apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Toast.makeText(getApplicationContext(), "Don't touch me!!", Toast.LENGTH_SHORT).show();
                hideProfileMenu();

                addNote.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            }
        });

    }

    public void generateRandomColor(){
        Red = (int) Math.abs(Math.random()*255);
        Green = (int) Math.abs(Math.random()*255);
        Blue = (int) Math.abs(Math.random()*255);
        int temp = Red + Green + Blue;
        if(temp < 100 || temp > 600){
            generateRandomColor();
        }
    }

    public void showProfileMenu(){
        profileMenu.setAlpha(0);
        profileMenu.setVisibility(View.VISIBLE);
        profileMenu.animate().alpha(1f).setDuration(100).start();
    }
    public void hideProfileMenu(){
        profileMenu.setAlpha(1f);
        profileMenu.animate().alpha(0).setDuration(100).start();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                profileMenu.setVisibility(View.INVISIBLE);
            }
        }, 300);
    }
}
