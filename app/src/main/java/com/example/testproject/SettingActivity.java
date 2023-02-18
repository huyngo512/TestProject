package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

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

import java.sql.SQLOutput;

public class SettingActivity extends AppCompatActivity {

    public AppCompatButton homeButton, saveButton;

    public AppCompatButton[] buttons;

    public static String PACKAGE_NAME;
    Context context;

    Animation scaleUp, scaleDown;
    ConstraintLayout mLayout;
    ConstraintSet constraintSet;

    SharedPreferences sharedWidthPreferences, sharedHeightPreferences;
    SharedPreferences.Editor editorWidth, editorHeight;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        context = SettingActivity.this;
        ExtensionMethod.hideNavigationBar(context);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        buttons = new AppCompatButton[20];

        sharedWidthPreferences = getSharedPreferences(PACKAGE_NAME + ".location.button.width", Context.MODE_PRIVATE);
        sharedHeightPreferences = getSharedPreferences(PACKAGE_NAME + ".location.button.height", Context.MODE_PRIVATE);

        editorWidth = sharedWidthPreferences.edit();
        editorHeight = sharedHeightPreferences.edit();

        constraintSet = new ConstraintSet();
        mLayout = findViewById(R.id.setting_layout);
        constraintSet.clone(mLayout);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        for (int i = 0; i < 20; i++) {
            int index = i + 1;
            String id = "button" + index + "_setting";
            buttons[i] = findViewById(getResources().getIdentifier(id, "id", getPackageName()));
            setLocationButton(buttons[i], index);
            buttons[i].setOnTouchListener((view, motionEvent) -> {
                checkTouch(motionEvent, buttons[index - 1], index);
                return true;
            });
        }
        constraintSet.applyTo(mLayout);


        homeButton = findViewById(R.id.home_main_setting);
        saveButton = findViewById(R.id.save_setting);

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

        saveButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                saveButton.startAnimation(scaleUp);
                //setData();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //setData();
                editorWidth.apply();
                editorHeight.apply();
                saveButton.startAnimation(scaleDown);
            }
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ExtensionMethod.hideNavigationBar(context);
    }

    public void checkTouch(MotionEvent motionEvent, AppCompatButton button, int index) {
        final float x = motionEvent.getRawX();
        final float y = motionEvent.getRawY();
        float widthRadius = (float) button.getWidth() / 2;
        float heightRadius = (float) button.getHeight() / 2;
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if (x >= widthRadius && x <= (mLayout.getWidth() - widthRadius)) {
                    float hBias = (x - widthRadius) / (mLayout.getWidth() - button.getWidth());
                    constraintSet.setHorizontalBias(button.getId(), hBias);
                    editorWidth.putFloat(String.valueOf(index), hBias);
                }
                if (y >= heightRadius && y <= (mLayout.getHeight() - heightRadius)) {
                    float vBias = (y - widthRadius) / (mLayout.getHeight() - button.getHeight());
                    constraintSet.setVerticalBias(button.getId(), vBias);
                    editorHeight.putFloat(String.valueOf(index), vBias);
                }
                constraintSet.applyTo(mLayout);
                break;
        }
    }


    public void setLocationButton(AppCompatButton button, int index) {
        if (sharedWidthPreferences.getFloat(String.valueOf(index), 0) != 0) {
            constraintSet.setHorizontalBias(button.getId(), sharedWidthPreferences.getFloat(String.valueOf(index), 0));
        }
        if (sharedHeightPreferences.getFloat(String.valueOf(index), 0) != 0) {
            constraintSet.setVerticalBias(button.getId(), sharedHeightPreferences.getFloat(String.valueOf(index), 0));
        }
    }
}