package com.elselse.doggoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("Accounts", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        TextView welcomeTxt = findViewById(R.id.wlcmTxt);
        welcomeTxt.setText(prefs.getString(prefs.getString("Logged", "")+"name",""));
        ConstraintLayout constraintLayout = findViewById(R.id.cL);
        ImageButton logoutButton = findViewById(R.id.logoutBtn);
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


                return false;
            }
        });

    }
}