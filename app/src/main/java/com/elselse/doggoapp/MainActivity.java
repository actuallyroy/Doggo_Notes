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
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends Activity {

    int Red, Green, Blue;
    LinearLayout profileMenu;
    String userName;
    EditText reminderDate, dueDate, descriptionText, titleText;
    boolean inputTimeErr = false;
    final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    boolean timeTextClicked = false;
    LinearLayout taskView, notesView, addNote;
    SharedPreferences notesPrefs, tasksPrefs;
    SharedPreferences.Editor notesPrefsEdit, tasksPrefsEditor;
    Drawable drawable;
    TextView title, textRepeat, textDue;
    RadioGroup repeatRadioGrp, dueRadioGrp, getRemRadioGrp;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = this.getWindow();
        window.setStatusBarColor(Color.rgb(0,209,187));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences
        SharedPreferences prefs = getSharedPreferences("Accounts", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        userName = prefs.getString("Logged", "");
        String name = prefs.getString(userName+"name","");

        notesPrefs = getSharedPreferences(userName+"Notes", MODE_PRIVATE);
        tasksPrefs = getSharedPreferences(userName+"Tasks", MODE_PRIVATE);
        notesPrefsEdit = notesPrefs.edit();
        tasksPrefsEditor = tasksPrefs.edit();

        String colorString = prefs.getString(userName+"Color","000000000");
        Red = Integer.parseInt(colorString.substring(0,3));
        Green = Integer.parseInt(colorString.substring(3,6));
        Blue = Integer.parseInt(colorString.substring(6,9));
        if(Red == 0 || Green == 0 || Blue == 0){
            generateRandomColor();
            prefsEditor.putString(userName+"Color", String.format("%03d", Red)+String.format("%03d", Green)+String.format("%03d", Blue));
            prefsEditor.apply();
        }
        ///////////////////////////////////

        String list = tasksPrefs.getString("List", "");
        for(String a: list.split(":")) {
            String task1 = tasksPrefs.getString(a, "");
            Log.d(a, task1);
        }


        //Views
        profileMenu = findViewById(R.id.profile_menu);
        TextView welcomeTxt = findViewById(R.id.wlcmTxt),
                userNameTxt = findViewById(R.id.userNameTxt),
                aboutBtn = findViewById(R.id.aboutBtn);

                textDue = findViewById(R.id.textDue);
                textRepeat = findViewById(R.id.textRepeat);
                title = findViewById(R.id.title);

        ConstraintLayout constraintLayout = findViewById(R.id.cL);
        ScrollView notesScrollView = findViewById(R.id.notesScrollView);

        ImageButton logoutButton = findViewById(R.id.logoutBtn);

        addNote = findViewById(R.id.addNote);
        taskView = findViewById(R.id.taskView);
        notesView = findViewById(R.id.notesView);

        FloatingActionButton fab = findViewById(R.id.fab);

        Button doneBtn = findViewById(R.id.doneBtn),
                cancelBtn = findViewById(R.id.cancel_button);

        reminderDate = findViewById(R.id.reminderDate);
        dueDate = findViewById(R.id.dueDate);

        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);

        getRemRadioGrp = findViewById(R.id.getRemRadioGrp);
        dueRadioGrp = findViewById(R.id.dueRadioGrp);
        repeatRadioGrp = findViewById(R.id.repeatRadioGrp);

        getRemRadioGrp.check(R.id.remRadioBtnOne);
        repeatRadioGrp.check(R.id.repeatRadioGrpNoRepeat);
        dueRadioGrp.check(R.id.dueRadioGrpNo);
        /////////////////////////////////

        //SetTasks
        setTasks();
        setNotes();


        //RadioButtons
        getRemRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.remRadioBtnOne){
                    title.setText("Add Note");
                    descriptionText.setVisibility(View.VISIBLE);
                    reminderDate.setVisibility(View.GONE);
                    textRepeat.setVisibility(View.GONE);
                    repeatRadioGrp.setVisibility(View.GONE);
                    //textDue.setVisibility(View.GONE);
                    //dueRadioGrp.setVisibility(View.GONE);
                    //dueDate.setVisibility(View.GONE);
                }else{
                    title.setText("Add Task");
                    descriptionText.setVisibility(View.GONE);
                    reminderDate.setVisibility(View.VISIBLE);
                    textRepeat.setVisibility(View.VISIBLE);
                    repeatRadioGrp.setVisibility(View.VISIBLE);
                    //textDue.setVisibility(View.VISIBLE);
                    //dueRadioGrp.setVisibility(View.VISIBLE);
                    //dueDate.setVisibility(View.VISIBLE);
                    if(i == R.id.remRadioBtnTwo){
                        setTime(1);
                    }
                    if(i == R.id.remRadioBtnThree){
                        setTime(24);
                    }
                }
            }
        });
        ///////////

        drawable = getDrawable(R.drawable.ic_cancel_icon);

        //TextViews
        welcomeTxt.setText(name);
        userNameTxt.setText(name.substring(0,1));
        userNameTxt.setTextColor(Color.rgb(Red, Green, Blue));
        /////////////////////

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
                        if (!titleText.getText().toString().equals("")) {
                            if (getRemRadioGrp.getCheckedRadioButtonId() == R.id.remRadioBtnOne) {
                                String n = notesPrefs.getString("List", "");
                                if (!n.contains(titleText.getText().toString())) {
                                    notesPrefsEdit.putString(titleText.getText().toString(), descriptionText.getText().toString() + "::" + System.currentTimeMillis());

                                    if(n.equals(""))
                                        notesPrefsEdit.putString("List", titleText.getText().toString());
                                    else
                                        notesPrefsEdit.putString("List", n + ":" + titleText.getText().toString());

                                    notesPrefsEdit.apply();
                                    titleText.setText("");
                                    descriptionText.setText("");
                                    getRemRadioGrp.check(R.id.remRadioBtnOne);
                                    addNote.setVisibility(View.INVISIBLE);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Choose another Title for your note", Toast.LENGTH_SHORT).show();
                                }
                                setTasks();
                            } else {
                                String n = tasksPrefs.getString("List", "");
                                if (!n.contains(titleText.getText().toString()) && !inputTimeErr) {
                                    String newTask = reminderDate.getText().toString();
                                    switch (repeatRadioGrp.getCheckedRadioButtonId()) {
                                        case R.id.repeatRadioGrpNoRepeat:
                                            newTask = newTask + "::N";
                                            break;
                                        case R.id.repeatRadioGrpDaily:
                                            newTask = newTask + "::D";
                                            break;
                                        case R.id.repeatRadioGrpWeekly:
                                            newTask = newTask + "::W";
                                            break;
                                    }

                                    tasksPrefsEditor.putString(titleText.getText().toString(), newTask + "::" + System.currentTimeMillis());
                                    if(n.equals(""))
                                        tasksPrefsEditor.putString("List", titleText.getText().toString());
                                    else
                                        tasksPrefsEditor.putString("List", n + ":" + titleText.getText().toString());

                                    tasksPrefsEditor.apply();
                                    titleText.setText("");
                                    descriptionText.setText("");
                                    getRemRadioGrp.check(R.id.remRadioBtnOne);
                                    addNote.setVisibility(View.INVISIBLE);
                                } else {
                                    if(n.contains(titleText.getText().toString()))
                                        Toast.makeText(getApplicationContext(), "Choose another Title", Toast.LENGTH_SHORT).show();
                                }
                            }
                            setNotes();
                        }
                        drawable.clearColorFilter();
                    }
                },200);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawable.setColorFilter(Color.rgb(1,219,197), PorterDuff.Mode.MULTIPLY);
                view.setBackground(drawable);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideAddNote();
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
                hideProfileMenu();
            }
        });

        reminderDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Drawable noError = getDrawable(R.drawable.custom_text_field),
                        errorDrawable = getDrawable(R.drawable.custom_error_text_field);
                try {
                    Date date = SDF.parse(charSequence.toString()),
                            now = new Date();
                    if(now.after(date)){
                        inputTimeErr = true;
                        reminderDate.setBackground(errorDrawable);
                    }else{
                        reminderDate.setBackground(noError);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    inputTimeErr = true;
                    reminderDate.setBackground(errorDrawable);
                }
                timeTextClicked = false;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        taskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAddNote();
            }
        });

        notesScrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAddNote();
            }
        });

        reminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = reminderDate.getSelectionStart();
                if (timeTextClicked) {
                    if(getRemRadioGrp.getCheckedRadioButtonId() == R.id.remRadioBtnTwo) {
                        if (reminderDate.getText().toString().contains("AM")) {
                            reminderDate.setText(reminderDate.getText().toString().replace("AM", "PM"));
                        } else {
                            reminderDate.setText(reminderDate.getText().toString().replace("PM", "AM"));
                        }
                    }else{
                        if (reminderDate.getText().toString().contains("AM")) {
                            reminderDate.setText(reminderDate.getText().toString().replace("AM", "PM"));
                        } else {
                            reminderDate.setText(reminderDate.getText().toString().replace("PM", "AM"));
                        }
                    }
                    reminderDate.setSelection(i);
                }
                timeTextClicked = true;
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


    public void setTime(int hrsDelay){
        Date now = new Date();
        now.setHours(now.getHours()+hrsDelay);
        String time = SDF.format(now);
        reminderDate.setText(time);
        dueDate.setText(time);
    }

    public void setTasks(){
        String[] tasks = tasksPrefs.getString("List", "").split(":");
        taskView.removeAllViews();
        for (String a : tasks) {
            if(!a.equals("")) {
                CheckBox taskCheckBox = new CheckBox(getApplicationContext());
                LinearLayout.LayoutParams lP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                taskCheckBox.setLayoutParams(lP);
                taskCheckBox.setText(a);
                taskCheckBox.setTextSize(20);
                taskCheckBox.setPadding(0, 50, 0, 50);
                taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Log.d(a, String.valueOf(b));
                    }
                });
                taskView.addView(taskCheckBox);
            }
        }
    }

    public void setNotes(){
        String[] notes = notesPrefs.getString("List", "").split(":");
        Log.d("note", Arrays.toString(notes));
        Drawable whiteDrawable = getDrawable(R.drawable.white);
        notesView.removeAllViews();
        for (String a : notes) {
            if(!a.equals("")) {
                EditText noteTitle = new EditText(getApplicationContext()),
                        noteDescription = new EditText(getApplicationContext());
                LinearLayout.LayoutParams lP1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                noteTitle.setLayoutParams(lP1);
                noteTitle.setEnabled(false);
                noteTitle.setTextSize(25);
                noteTitle.setText(a);
                noteTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                noteTitle.setTextColor(Color.DKGRAY);
                noteTitle.setBackground(whiteDrawable);
                noteDescription.setLayoutParams(lP1);
                noteDescription.setEnabled(false);
                noteDescription.setPadding(0, 0, 0, 20);
                noteDescription.setTextSize(20);
                noteDescription.setTextColor(Color.DKGRAY);
                noteDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                String temp = notesPrefs.getString(a, "");
                if (temp.length() > 15)
                    noteDescription.setText(temp.substring(0, temp.length() - 15));

                noteDescription.setBackground(getDrawable(R.drawable.desc_bg));
                notesView.addView(noteTitle);
                notesView.addView(noteDescription);
            }
        }
    }

    public void showAddNote(){
        addNote.setVisibility(View.VISIBLE);
    }

    public void hideAddNote(){
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
        getRemRadioGrp.check(R.id.remRadioBtnOne);
    }

}
