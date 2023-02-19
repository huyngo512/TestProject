package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PracticeActivity extends AppCompatActivity {

    public AppCompatButton[] buttons;

    public Dialog dialog;

    public static int NUMBER_OF_BUTTONS = 20;

    public boolean check = false;

    public int index_test, option;

    AppCompatButton homeButton;
    Animation scaleUp, scaleDown;

    Context context;
    public static String PACKAGE_NAME;

    SharedPreferences sharedPreferences, sharedWidthPreferences, sharedHeightPreferences, dataPreferences, rulesPreferences;

    TextView textView, displayTextview;

    ArrayList<HashMap<String, Integer>> rules;

    public String[] word = {"", "", "", ""};

    ConstraintLayout mLayout;
    ConstraintSet constraintSet;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        index_test = 1;

        buttons = new AppCompatButton[NUMBER_OF_BUTTONS];

        dialog = new Dialog(this);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        context = PracticeActivity.this;
        ExtensionMethod.hideNavigationBar(context);

        mLayout = findViewById(R.id.practice_layout);
        constraintSet = new ConstraintSet();
        constraintSet.clone(mLayout);

        rules = ExtensionMethod.getRules();

        sharedPreferences = getSharedPreferences(PACKAGE_NAME + ".dictionary", Context.MODE_PRIVATE);
        sharedWidthPreferences = getSharedPreferences(PACKAGE_NAME + ".location.button.width", Context.MODE_PRIVATE);
        sharedHeightPreferences = getSharedPreferences(PACKAGE_NAME + ".location.button.height", Context.MODE_PRIVATE);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        homeButton = findViewById(R.id.home_practice);

        textView = findViewById(R.id.textView_practice);
        displayTextview = findViewById(R.id.textView_display_practice);



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

        Intent intent = getIntent();
        option = intent.getIntExtra("option", 1);

        if (option == 0) {
            dataPreferences = getSharedPreferences(PACKAGE_NAME + ".amdau.data", Context.MODE_PRIVATE);
            rulesPreferences = getSharedPreferences(PACKAGE_NAME + ".amdau", Context.MODE_PRIVATE);
        }
        if (option == 1) {
            dataPreferences = getSharedPreferences(PACKAGE_NAME + ".amchinh.data", Context.MODE_PRIVATE);
            rulesPreferences = getSharedPreferences(PACKAGE_NAME + ".amchinh", Context.MODE_PRIVATE);
        }
        if (option == 2) {
            dataPreferences = getSharedPreferences(PACKAGE_NAME + ".amcuoi.data", Context.MODE_PRIVATE);
            rulesPreferences = getSharedPreferences(PACKAGE_NAME + ".amcuoi", Context.MODE_PRIVATE);
        }

        for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {
            int index = i + 1;
            String id = "button" + index + "_practice";
            buttons[i] = findViewById(getResources().getIdentifier(id, "id", getPackageName()));
            setLocationButton(buttons[i], index);
            buttons[i].setOnTouchListener((view, motionEvent) -> {
                checkTouch(motionEvent, buttons[index - 1], Integer.parseInt(buttons[index - 1].getTag().toString()));
                return true;
            });
        }
        constraintSet.applyTo(mLayout);

        textView.setText(dataPreferences.getString(String.valueOf(index_test), ""));
        setColorButton(dataPreferences.getString(String.valueOf(index_test), ""), option);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ExtensionMethod.hideNavigationBar(context);
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
                    displayTextview.setText(tmp);
                    Arrays.fill(word, "");
                    check = false;
                    if (tmp.equals(dataPreferences.getString(String.valueOf(index_test), ""))) {
                        if(checkEndPractice(option, index_test)){
                            openDialog();
                        }else{
                            index_test++;
                            textView.setText(dataPreferences.getString(String.valueOf(index_test), ""));
                        }
                    }

                }
                button.setPressed(false);
                setColorButton(dataPreferences.getString(String.valueOf(index_test), ""), option);
                break;

            case MotionEvent.ACTION_POINTER_UP:

                button.startAnimation(scaleUp);
                if (check) {
                    String tmp = ExtensionMethod.matchWord(word[0], word[1], word[2]);
                    displayTextview.setText(tmp);
                    Arrays.fill(word, "");
                    check = false;
                    if (tmp.equals(dataPreferences.getString(String.valueOf(index_test), ""))) {
                        if(checkEndPractice(option, index_test)){
                            openDialog();
                        }else{
                            index_test++;
                            textView.setText(dataPreferences.getString(String.valueOf(index_test), ""));
                        }
                    }
                }
                button.setPressed(false);
                setColorButton(dataPreferences.getString(String.valueOf(index_test), ""), option);

            case MotionEvent.ACTION_CANCEL:

                button.startAnimation(scaleUp);
                if (check) {
                    String tmp = ExtensionMethod.matchWord(word[0], word[1], word[2]);
                    displayTextview.setText(tmp);
                    Arrays.fill(word, "");
                    check = false;
                    if (tmp.equals(dataPreferences.getString(String.valueOf(index_test), ""))) {
                        if(checkEndPractice(option, index_test)){
                            openDialog();
                        }else{
                            index_test++;
                            textView.setText(dataPreferences.getString(String.valueOf(index_test), ""));
                        }
                    }
                }
                button.setPressed(false);
                setColorButton(dataPreferences.getString(String.valueOf(index_test), ""), option);
                break;
        }


    }

    public void setColorButton(String text, int option) {
        char[] arr = text.toCharArray();
        if (option == 1 && String.valueOf(arr[0]).equals("N")) {
            buttons[15].setPressed(true);
        }
        for (char tmp : arr) {
            for (AppCompatButton button : buttons) {
                if (button.getText().toString().equals(String.valueOf(tmp)) && Integer.parseInt(button.getTag().toString()) == option) {
                    button.setPressed(true);
                    break;
                }
            }
        }
    }
    private  void openDialog () {
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        AppCompatButton btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), OptionPracticeActivity.class);
            dialog.dismiss();
            startActivity(intent);
        });
        dialog.show();
    }

    public boolean checkEndPractice(int option, int index){
        if((option == 0 && index == 26) || (option == 1 && index == 172) || (option == 2 && index == 12)){
            return true;
        }
        return false;
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