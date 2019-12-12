package com.example.vspot4.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref yourPreference;
    private SharedPreferences sharedPref;

    public SharedPref(Context context) {
        sharedPref = context.getSharedPreferences("VSPOT_API_KEY", Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new SharedPref(context);
        }
        return yourPreference;
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getData(String key) {
        if (sharedPref != null) {
            return sharedPref.getString(key, "");
        }
        return "";
    }
}
