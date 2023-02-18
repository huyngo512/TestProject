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
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static int NUMBER_OF_BUTTONS = 20;

    public AppCompatButton[] buttons;

    public AppCompatButton homeButton;

    public static String PACKAGE_NAME;

    SharedPreferences sharedPreferences, sharedWidthPreferences, sharedHeightPreferences;

    Context context;

    public ConstraintLayout mLayout;
    public ConstraintSet constraintSet;

    public TextView textView, displayText;
    public boolean check = false;

    Animation scaleUp, scaleDown;

    ArrayList<HashMap<String, Integer>> rules;


    public String[] word = {"", "", "", ""};
    public String completeWord = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rules = ExtensionMethod.getRules();

        context = MainActivity.this;
        ExtensionMethod.hideNavigationBar(context);

        buttons = new AppCompatButton[NUMBER_OF_BUTTONS];

        PACKAGE_NAME = getApplicationContext().getPackageName();

        mLayout = findViewById(R.id.main_layout);
        constraintSet = new ConstraintSet();
        constraintSet.clone(mLayout);


        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        sharedPreferences = getSharedPreferences(PACKAGE_NAME + ".dictionary", Context.MODE_PRIVATE);
        sharedWidthPreferences = getSharedPreferences(PACKAGE_NAME + ".location.button.width", Context.MODE_PRIVATE);
        sharedHeightPreferences = getSharedPreferences(PACKAGE_NAME + ".location.button.height", Context.MODE_PRIVATE);

        for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {
            int index = i + 1;
            String id = "button" + index;
            buttons[i] = findViewById(getResources().getIdentifier(id, "id", getPackageName()));
            setLocationButton(buttons[i], index);
            buttons[i].setOnTouchListener((view, motionEvent) -> {
                checkTouch(motionEvent, buttons[index - 1], Integer.parseInt(buttons[index - 1].getTag().toString()));
                return true;
            });
        }

        constraintSet.applyTo(mLayout);

        homeButton = findViewById(R.id.home_main);
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

        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        displayText = findViewById(R.id.textView_display);

    }

    public void checkTouch(MotionEvent motionEvent, AppCompatButton button, int typeWord) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                check = true;
                button.startAnimation(scaleDown);
                button.setPressed(true);

                if (word[typeWord].equals("")) {
                    word[typeWord] = word[typeWord].concat(button.getText().toString());

                } else {
                    char[] tmp = word[typeWord].toCharArray();
                    if (rules.get(typeWord).get(Character.toString(tmp[tmp.length - 1])) < rules.get(typeWord).get(button.getText().toString())) {
                        word[typeWord] = word[typeWord].concat(button.getText().toString());

                    } else {
                        for (int i = 0; i < tmp.length; i++) {
                            if (rules.get(typeWord).get(Character.toString(tmp[i])) > rules.get(typeWord).get(button.getText().toString())) {
                                word[typeWord] = new StringBuilder(word[typeWord]).insert(i, button.getText().toString()).toString();
                                break;
                            }
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                button.startAnimation(scaleDown);

                if (word[typeWord].equals("")) {
                    word[typeWord] = word[typeWord].concat(button.getText().toString());

                } else {
                    char[] tmp = word[typeWord].toCharArray();
                    if (rules.get(typeWord).get(Character.toString(tmp[tmp.length - 1])) < rules.get(typeWord).get(button.getText().toString())) {
                        word[typeWord] = word[typeWord].concat(button.getText().toString());

                    } else {
                        for (int i = 0; i < tmp.length; i++) {
                            if (rules.get(typeWord).get(Character.toString(tmp[i])) > rules.get(typeWord).get(button.getText().toString())) {
                                word[typeWord] = new StringBuilder(word[typeWord]).insert(i, button.getText().toString()).toString();
                                break;
                            }
                        }
                    }
                }

                button.setPressed(true);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                button.startAnimation(scaleUp);

                if (check) {
                    String tmp = ExtensionMethod.matchWord(word[0], word[1], word[2]);
                    String text = sharedPreferences.getString(tmp, "");
                    if (!text.equals("")) {
                        textView.append(text + " ");
                    }
                    displayText.setText(tmp);
                    completeWord = "";
                    Arrays.fill(word, "");
                    check = false;
                }
                button.setPressed(false);
                break;

            case MotionEvent.ACTION_POINTER_UP:

                button.startAnimation(scaleUp);
                if (check) {
                    String tmp = ExtensionMethod.matchWord(word[0], word[1], word[2]);
                    String text = sharedPreferences.getString(tmp, "");
                    if (!text.equals("")) {
                        textView.append(text + " ");
                    }
                    displayText.setText(tmp);
                    completeWord = "";
                    Arrays.fill(word, "");
                    check = false;
                }
                button.setPressed(false);

            case MotionEvent.ACTION_CANCEL:

                button.startAnimation(scaleUp);
                if (check) {
                    String tmp = ExtensionMethod.matchWord(word[0], word[1], word[2]);
                    String text = sharedPreferences.getString(tmp, "");
                    if (!text.equals("")) {
                        textView.append(text + " ");
                    }
                    displayText.setText(tmp);

                    completeWord = "";
                    Arrays.fill(word, "");
                    check = false;
                }
                button.setPressed(false);
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        ExtensionMethod.hideNavigationBar(context);
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