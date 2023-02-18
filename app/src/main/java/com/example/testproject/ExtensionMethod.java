package com.example.testproject;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class ExtensionMethod {

    public static void hideNavigationBar(Context context) {
        ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    public static ArrayList<HashMap<String, Integer>> getRules() {
        ArrayList<HashMap<String, Integer>> rules = new ArrayList<>();

        HashMap<String, Integer> amdau, amchinh, amcuoi;

        amdau = new HashMap<>();
        amchinh = new HashMap<>();
        amcuoi = new HashMap<>();

        amdau.put("S", 1);
        amdau.put("T", 2);
        amdau.put("K", 3);
        amdau.put("P", 4);
        amdau.put("R", 5);
        amdau.put("H", 6);
        amdau.put("N", 7);

        rules.add(amdau);

        amchinh.put("H", 2);
        amchinh.put("S", 3);
        amchinh.put("*", 4);
        amchinh.put("I", 5);
        amchinh.put("U", 6);
        amchinh.put("O", 7);
        amchinh.put("E", 8);
        amchinh.put("A", 9);
        amchinh.put("W", 10);
        amchinh.put("Y", 11);

        rules.add(amchinh);

        amcuoi.put("J", 1);
        amcuoi.put("N", 2);
        amcuoi.put("G", 3);
        amcuoi.put("T", 4);
        amcuoi.put("K", 5);

        rules.add(amcuoi);

        return rules;
    }
    public static String matchWord(String firstWord, String secondWord, String thirdWord){
        String mathWord;
        if(firstWord.length() == 0 && thirdWord.length() == 0 && secondWord.startsWith("*")){
            String tmp = secondWord.substring(1);
            mathWord = ("*-").concat(tmp);
        }else{
            if(firstWord.length() > 0 && (firstWord.contains("N")) && secondWord.startsWith("*")){
                String tmp = secondWord.substring(1);
                mathWord = firstWord.concat("*-").concat(tmp).concat(thirdWord);
            }else {
                mathWord = firstWord.concat("-").concat(secondWord).concat(thirdWord);
            }
        }
        return mathWord;
    }

}
