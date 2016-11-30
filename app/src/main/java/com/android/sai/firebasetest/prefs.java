package com.android.sai.firebasetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by sai on 5/11/16.
 */

public class prefs {
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String formatKey(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s,"UTF8");
        encode = encode.replace(".","");
        encode = encode.replace("#","");
        encode = encode.replace("$","");
        encode = encode.replace("[","");
        encode = encode.replace("]","");
        encode = encode.replace("%","");
        return encode;
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
