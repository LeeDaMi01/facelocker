package com.example.flocker;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    //사용자의 아이디를 저장하기 위해 만듬. int타입만 만들었으나, 필요에 따라 여러 타입 추가 가능
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public static int getInt(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, 1);
        return value;
    }
}
