package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView scannedText;
    private Button copyBtn, scanBtn, scanAgainBtn;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        int[] status = new int[] {scannedText.getVisibility(), scanBtn.getVisibility(), scanAgainBtn.getVisibility(), copyBtn.getVisibility()};
        boolean[] enablingStatus= new boolean[]{scanBtn.isEnabled(), scanAgainBtn.isEnabled(), copyBtn.isEnabled()};


        outState.putString("scannedText", scannedText.getText().toString());
//        outState.putInt("scannedTextStatus", scannedText.getVisibility());
        outState.putIntArray("status", status);
        outState.putBooleanArray("enablingStatus", enablingStatus);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        scannedText.setText(savedInstanceState.getString("scannedText"));
//        scannedText.setVisibility(savedInstanceState.getInt("scannedTextStatus"));
        int[] status= Objects.requireNonNull(savedInstanceState.getIntArray("status")).clone();
        boolean[] enablingStatus= Objects.requireNonNull(savedInstanceState.getBooleanArray("enablingStatus")).clone();

        scannedText.setVisibility(status[0]);
        scanBtn.setVisibility(status[1]);
        scanAgainBtn.setVisibility(status[2]);
        copyBtn.setVisibility(status[3]);

        scanBtn.setEnabled(enablingStatus[0]);
        scanAgainBtn.setEnabled(enablingStatus[1]);
        copyBtn.setEnabled(enablingStatus[2]);


    }

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
        copyFn();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (intentResult!=null){
            if (intentResult.getContents()==null) {
                scannedText.setText("Error : Scanning Failed");
                copyBtn.setEnabled(false);
                scanBtn.setText("Scan Again");
                scannedText.setVisibility(View.VISIBLE);

            }


            else {
                scannedText.setText(intentResult.getContents());
                copyBtn.setEnabled(true);
                scannedText.setVisibility(View.VISIBLE);
                scanBtn.setVisibility(View.GONE);
                scanBtn.setEnabled(false);
                copyBtn.setVisibility(View.VISIBLE);
                scanAgainBtn.setVisibility(View.VISIBLE);

            }




        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void copyFn(){
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("TextView", scannedText.getText().toString());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
