package com.elselse.doggoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends Activity {

    int Red, Green, Blue;
    LinearLayout profileMenu;
    String userName;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        refreshActivity();
        SharedPreferences prefs = getSharedPreferences("Accounts", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        profileMenu = findViewById(R.id.profile_menu);
        TextView welcomeTxt = findViewById(R.id.wlcmTxt),
                userNameTxt = findViewById(R.id.userNameTxt),
                aboutBtn = findViewById(R.id.aboutBtn),
                textRepeat = findViewById(R.id.textRepeat),
                textDue = findViewById(R.id.textDue),
                title = findViewById(R.id.title);

        LinearLayout addNote = findViewById(R.id.addNote);
        FloatingActionButton fab = findViewById(R.id.fab);
        Button doneBtn = findViewById(R.id.doneBtn),
                cancelBtn = findViewById(R.id.cancel_button);

        EditText reminderDate = findViewById(R.id.reminderDate),
                dueDate = findViewById(R.id.dueDate),
                titleText = findViewById(R.id.titleText),
                descriptionText = findViewById(R.id.descriptionText);

        RadioGroup getRemRadioGrp = findViewById(R.id.getRemRadioGrp),
                repeatRadioGrp = findViewById(R.id.repeatRadioGrp),
                dueRadioGrp = findViewById(R.id.dueRadioGrp);

        getRemRadioGrp.check(R.id.remRadioBtnOne);
        repeatRadioGrp.check(R.id.repeatRadioGrpNoRepeat);
        dueRadioGrp.check(R.id.dueRadioGrpNo);

        getRemRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.remRadioBtnOne){
                    title.setText("Add Note");
                    descriptionText.setVisibility(View.VISIBLE);
                    reminderDate.setVisibility(View.GONE);
                    textRepeat.setVisibility(View.GONE);
                    repeatRadioGrp.setVisibility(View.GONE);
                    textDue.setVisibility(View.GONE);
                    dueRadioGrp.setVisibility(View.GONE);
                    dueDate.setVisibility(View.GONE);
                }else{
                    title.setText("Add Task");
                    descriptionText.setVisibility(View.GONE);
                    reminderDate.setVisibility(View.VISIBLE);
                    textRepeat.setVisibility(View.VISIBLE);
                    repeatRadioGrp.setVisibility(View.VISIBLE);
                    textDue.setVisibility(View.VISIBLE);
                    dueRadioGrp.setVisibility(View.VISIBLE);
                    dueDate.setVisibility(View.VISIBLE);
                    if(i == R.id.remRadioBtnTwo){

                    }
                }
            }
        });
        userName = prefs.getString("Logged", "");
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                Date now = new Date();
                reminderDate.setText(sdf.format(now));
                dueDate.setText(sdf.format(now));
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
                        if(getRemRadioGrp.getCheckedRadioButtonId() == R.id.remRadioBtnOne) {
                            SharedPreferences prefs = getSharedPreferences(userName+"Notes", MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            prefsEditor.putString(titleText.getText().toString(), descriptionText.getText().toString());
                            prefsEditor.apply();
                            titleText.setText("");
                            descriptionText.setText("");
                            getRemRadioGrp.check(R.id.remRadioBtnOne);
                        }
                        drawable.clearColorFilter();
                        addNote.setVisibility(View.INVISIBLE);
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
                        title.setText("Add Note");
                        descriptionText.setVisibility(View.VISIBLE);
                        reminderDate.setVisibility(View.GONE);
                        textRepeat.setVisibility(View.GONE);
                        repeatRadioGrp.setVisibility(View.GONE);
                        textDue.setVisibility(View.GONE);
                        dueRadioGrp.setVisibility(View.GONE);
                        dueDate.setVisibility(View.GONE);
                        titleText.setText("");
                        descriptionText.setText("");
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

    public void refreshActivity(){
        SharedPreferences preferences = getSharedPreferences(userName+"Notes", MODE_PRIVATE);
        int i = preferences.getAll().size();
        Log.d("size", String.valueOf(i));
    }
}
