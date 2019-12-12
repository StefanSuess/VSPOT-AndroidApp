package com.example.vspot4.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vspot4.database.SharedPref;

public class MainActivity extends AppCompatActivity {

    // Result variables
    public static final int FIRST_START_REQUEST = 1;
    public static final int CAMERA_PERMISSION_REQUEST = 2;
    public static final int READ_QR_CODE = 3;
    // Static variables
    public static final String API_KEY = "API_KEY";
    public static Context context;
    public static View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Make public static context
        context = getApplicationContext();

        if (isFirstStart()) { // if first start
            Intent firstStartIntent = new Intent(this, FirstStartActivity.class);
            startActivityForResult(firstStartIntent, FIRST_START_REQUEST);
            finish(); // pop the stack
        } else { // if not first start
            Intent presentationIntent = new Intent(this, PresentationActivity.class);
            startActivity(presentationIntent);
            finish(); // pop the stack
        }
    }

    private Boolean isFirstStart() {
        // check if API_KEY is set, return true if API_KEY is empty
        SharedPref sharedPref = SharedPref.getInstance(getApplicationContext());
        String API_KEY = sharedPref.getData(MainActivity.API_KEY);
        return API_KEY.isEmpty();
    }
}
