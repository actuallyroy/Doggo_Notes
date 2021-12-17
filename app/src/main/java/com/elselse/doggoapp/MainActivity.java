package com.elselse.doggoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("Accounts", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        TextView welcomeTxt = findViewById(R.id.wlcmTxt);
        welcomeTxt.setText("Welcome, "+ prefs.getString(prefs.getString("Logged", "")+"name",""));
        ConstraintLayout constraintLayout = findViewById(R.id.cL);
        Button logoutButton = findViewById(R.id.logoutBtn);
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