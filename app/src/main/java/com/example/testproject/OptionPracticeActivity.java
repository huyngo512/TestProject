package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OptionPracticeActivity extends AppCompatActivity {

    AppCompatButton firstButton, secondButton, thirdButton, homeButton;
    Animation scaleUp, scaleDown;

    Context context;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_practice);

        context = OptionPracticeActivity.this;
        ExtensionMethod.hideNavigationBar(context);


        firstButton = findViewById(R.id.first_button);
        secondButton = findViewById(R.id.second_button);
        thirdButton = findViewById(R.id.third_button);
        homeButton = findViewById(R.id.home_option_practice);


        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);


        firstButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                firstButton.startAnimation(scaleUp);
                Intent intent = new Intent(getApplicationContext(), PracticeActivity.class);
                intent.putExtra("option", 0);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                firstButton.startAnimation(scaleDown);
            }
            return true;
        });

        secondButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                secondButton.startAnimation(scaleUp);
                Intent intent = new Intent(getApplicationContext(), PracticeActivity.class);
                intent.putExtra("option", 1);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                secondButton.startAnimation(scaleDown);
            }
            return true;
        });

        thirdButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                thirdButton.startAnimation(scaleUp);
                Intent intent = new Intent(getApplicationContext(), PracticeActivity.class);
                intent.putExtra("option", 2);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                thirdButton.startAnimation(scaleDown);
            }
            return true;
        });

        homeButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                homeButton.startAnimation(scaleUp);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                homeButton.startAnimation(scaleDown);
            }
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ExtensionMethod.hideNavigationBar(context);
    }

}