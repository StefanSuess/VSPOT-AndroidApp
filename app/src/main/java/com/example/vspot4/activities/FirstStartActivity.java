package com.example.vspot4.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vspot4.R;
import com.example.vspot4.database.SharedPref;

public class FirstStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_explanation);
    }


    // Request the camera permission / start QRCode Reader if already granted
    public void requestCameraPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MainActivity.CAMERA_PERMISSION_REQUEST);
        } else {
            startQRCodeReader();
        }
    }


    // check for result of permission request, if successful start QRCode Reader
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MainActivity.CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRCodeReader();
            } else {
                //TODO: Show further explanation
            }
        }
    }


    // start the QRCode Activity with result
    public void startQRCodeReader() {
        Intent QRCodeIntent = new Intent(this, QRCodeActivity.class);
        startActivityForResult(QRCodeIntent, MainActivity.READ_QR_CODE);
    }


    // Check if everything is ok, save API_KEY in sharedpreferences, return to MainActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request it is that we're responding to
        if (requestCode == MainActivity.READ_QR_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String API_KEY = data.getStringExtra(MainActivity.API_KEY);

                // Save API_KEY in SharedPreferences
                SharedPref sharedPref = SharedPref.getInstance(getApplicationContext());
                sharedPref.saveData(MainActivity.API_KEY, API_KEY);

                // Start another activity
                Intent mainActivityIntent = new Intent(this, PresentationActivity.class);
                startActivity(mainActivityIntent);
            }
        }
    }
}

