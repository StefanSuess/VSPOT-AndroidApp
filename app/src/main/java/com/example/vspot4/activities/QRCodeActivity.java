package com.example.vspot4.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);
        List<BarcodeFormat> myformat = new ArrayList<>();
        myformat.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(myformat);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // check if scanned code is a QR Code
        if (rawResult.getText().contains("/api/")) {
            // report back
            Intent returnIntent = new Intent();
            returnIntent.putExtra(MainActivity.API_KEY, rawResult.getText());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            // continue scanning if not the right code
            mScannerView.resumeCameraPreview(this);
            Toast.makeText(this, "API KEY Scannen", Toast.LENGTH_LONG).show();
        }
    }
}