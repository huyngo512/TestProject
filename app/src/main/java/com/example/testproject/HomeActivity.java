package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HomeActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;
    AppCompatButton keyboardButton, practiceButton, settingButton, instructButton, btnClose;
    Dialog dialog;
    Animation scaleUp, scaleDown;
    Context context;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = HomeActivity.this;
        ExtensionMethod.hideNavigationBar(context);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        dialog = new Dialog(this);

        readFile();

        readFileRaw("amdau");
        readFileRaw("amchinh");
        readFileRaw("amcuoi");


        keyboardButton = findViewById(R.id.keyboard_button);
        practiceButton = findViewById(R.id.practice_button);
        settingButton = findViewById(R.id.setting_button);
        instructButton = findViewById(R.id.instruct_button);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        keyboardButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                keyboardButton.startAnimation(scaleUp);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                keyboardButton.startAnimation(scaleDown);
            }
            return true;
        });
        practiceButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                practiceButton.startAnimation(scaleUp);
                Intent intent = new Intent(getApplicationContext(), OptionPracticeActivity.class);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                practiceButton.startAnimation(scaleDown);
            }
            return true;
        });
        settingButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                settingButton.startAnimation(scaleUp);
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                settingButton.startAnimation(scaleDown);
            }
            return true;
        });
        instructButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                instructButton.startAnimation(scaleUp);
                openDialog();
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                instructButton.startAnimation(scaleDown);
            }
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ExtensionMethod.hideNavigationBar(context);
    }

    //read data from the dictionary
    public void readFile() {
        BufferedReader reader;
        SharedPreferences sharedPreferences = getSharedPreferences(PACKAGE_NAME + ".dictionary", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            final InputStream file = getResources().openRawResource(getResources().getIdentifier("tailieu", "raw", getPackageName()));
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] part = line.split("\t");
                editor.putString(part[0], part[1]);
                line = reader.readLine();
            }
            editor.apply();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void readFileRaw(String document) {
        BufferedReader reader;
        SharedPreferences rulesPreferences, dataPreferences;
        rulesPreferences = getSharedPreferences(PACKAGE_NAME + "." + document, Context.MODE_PRIVATE);
        dataPreferences = getSharedPreferences(PACKAGE_NAME + "." + document + ".data", Context.MODE_PRIVATE);
        SharedPreferences.Editor rulesEditor, dataEditor;

        rulesEditor = rulesPreferences.edit();
        dataEditor = dataPreferences.edit();

        try {
            final InputStream file = getResources().openRawResource(getResources().getIdentifier(document, "raw", getPackageName()));
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            int i = 1;
            line = reader.readLine();
            while (line != null) {
                String[] part = line.split("\t");
                rulesEditor.putString(part[1], part[0]);
                dataEditor.putString(String.valueOf(i), part[1]);
                i++;
                line = reader.readLine();
            }
            rulesEditor.apply();
            dataEditor.apply();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void openDialog() {
        dialog.setContentView(R.layout.layout_instruct);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        AppCompatButton btnOK = dialog.findViewById(R.id.btnClose);
        btnOK.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }


}