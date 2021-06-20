package com.example.i_queue;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QrCodeScanner.this , MainPageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        Intent myIntent = new Intent(QrCodeScanner.this, MainPageActivity.class);
        myIntent.putExtra("readedData", result.getText());
        QrCodeScanner.this.startActivity(myIntent);
        finish();
    }
}
