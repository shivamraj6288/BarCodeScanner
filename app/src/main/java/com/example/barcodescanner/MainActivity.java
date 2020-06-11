package com.example.barcodescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private TextView scannedText;
    private Button copyBtn, scanBtn, scanAgainBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannedText=findViewById(R.id.scannedText);
        copyBtn=findViewById(R.id.copyText);
        scanBtn=findViewById(R.id.scanText);
        scanAgainBtn=findViewById(R.id.scanAgain);

        copyBtn.setEnabled(false);
        copyBtn.setVisibility(View.GONE);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Place code in front of Camera");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
                intentIntegrator.initiateScan();


            }
        });
        scanAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Place code in front of Camera");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
                intentIntegrator.initiateScan();



            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (intentResult!=null){
            if (intentResult.getContents()==null)
                scannedText.setText("Error : Scanning Failed");

            else {
                scannedText.setText(intentResult.getContents());
            }
            if (scanBtn.isEnabled()){
                scannedText.setVisibility(View.VISIBLE);
                scanBtn.setVisibility(View.GONE);
                scanBtn.setEnabled(false);
                copyBtn.setVisibility(View.VISIBLE);
                scanAgainBtn.setVisibility(View.VISIBLE);
            }



        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
