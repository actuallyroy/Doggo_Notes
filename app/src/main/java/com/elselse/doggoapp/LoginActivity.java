package com.elselse.doggoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends Activity {
    LinearLayout signUpView;
    TextView createAccTxt;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("Accounts", MODE_PRIVATE);
        prefsEditor = prefs.edit();

        createAccTxt = findViewById(R.id.create_acc_txt);
        signUpView = findViewById(R.id.sign_up_view);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        linearLayout.setAlpha(0);
        linearLayout.setY(linearLayout.getY()-100);
        linearLayout.animate().alpha(1f).setDuration(1000).yBy(100).start();
        Button loginBtn = findViewById(R.id.login_btn),
                signUpBtn = findViewById(R.id.sign_up_button);
        EditText userNameForLogin = findViewById(R.id.user_name_for_login),
                passwordForLogin = findViewById(R.id.password_for_login),
                nameForSignUp = findViewById(R.id.name_for_signup),
                userNameForSignUp = findViewById(R.id.username_for_signup),
                passwordForSignUp = findViewById(R.id.password_for_signup),
                confPasswordForSignUp = findViewById(R.id.conf_password_for_signup);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String username = userNameForLogin.getText().toString(),
                        password = passwordForLogin.getText().toString();
                try {
                    Log.d("SHA", toHexString(getSHA(password)));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if(!username.equals("") || !password.equals("")) {
                    try {
                        if (prefs.getString(username + "password", "").equals(toHexString(getSHA(password)))) {
                            prefsEditor.putString("Logged", username);
                            prefsEditor.apply();
                            login();
                        } else if (prefs.getString(username + "password", "").equals("")) {
                            Toast.makeText(getApplicationContext(), "Username doesn't exist in the database.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameForSignUp.getText().toString(),
                        username = userNameForSignUp.getText().toString(),
                        password = passwordForSignUp.getText().toString(),
                        confPass = confPasswordForSignUp.getText().toString();
                if(name.equals("")){
                    nameForSignUp.setBackground(getDrawable(R.drawable.custom_error_text_field));
                }
                if(username.equals("")){
                    userNameForSignUp.setBackground(getDrawable(R.drawable.custom_error_text_field));
                }
                if(password.equals("") || !confPass.equals(password)){
                    passwordForSignUp.setBackground(getDrawable(R.drawable.custom_error_text_field));
                }
                if(confPass.equals("") || !confPass.equals(password)){
                    confPasswordForSignUp.setBackground(getDrawable(R.drawable.custom_error_text_field));
                }
                if(!name.equals("") && !username.equals("") && !password.equals("") && confPass.equals(password)){
                    if(prefs.getString(username+"name","").equals("")) {
                        prefsEditor.putString(username+"name", name);
                        try {
                            prefsEditor.putString(username+"password", toHexString(getSHA(password)));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        prefsEditor.apply();
                        Toast.makeText(getApplicationContext(), "Successfully registered.", Toast.LENGTH_LONG).show();
                        hideRegView();
                    }else{
                        userNameForSignUp.setBackground(getDrawable(R.drawable.custom_error_text_field));
                        Toast.makeText(getApplicationContext(), "Username already Exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("touched", "tou");
                hideRegView();
            }
        });

        userNameForSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userNameForSignUp.setBackground(getDrawable(R.drawable.custom_text_field));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordForSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordForSignUp.setBackground(getDrawable(R.drawable.custom_text_field));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameForSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameForSignUp.setBackground(getDrawable(R.drawable.custom_text_field));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confPasswordForSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confPasswordForSignUp.setBackground(getDrawable(R.drawable.custom_text_field));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordForLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    loginBtn.performClick();
                }
                return true;
            }
        });
        passwordForSignUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    signUpBtn.performClick();
                }
                return false;
            }
        });

        createAccTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable customTextField = getDrawable(R.drawable.custom_text_field);
                signUpView.setAlpha(0f);
                signUpView.animate().alpha(1f).setDuration(300).start();
                signUpView.setVisibility(View.VISIBLE);
                createAccTxt.setClickable(false);
                nameForSignUp.setBackground(customTextField);
                userNameForSignUp.setBackground(customTextField);
                passwordForSignUp.setBackground(customTextField);
                confPasswordForSignUp.setBackground(customTextField);
            }
        });

        signUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(!prefs.getString("Logged", "").equals("")){
            login();
        }
    }

    @Override
    public void onBackPressed() {
        if(signUpView.getVisibility() == View.INVISIBLE){
            super.onBackPressed();
        }else {
            hideRegView();
        }
    }

    public void hideRegView(){
        signUpView.animate().alpha(0).setDuration(300).start();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                signUpView.setVisibility(View.INVISIBLE);
                createAccTxt.setClickable(true);
            }
        }, 300);
    }

    private void login(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}