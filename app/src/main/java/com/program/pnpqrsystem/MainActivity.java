package com.program.pnpqrsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.program.pnpqrsystem.report.ReportActivity;
import com.program.pnpqrsystem.report.ReportSelectionActivity;
import com.program.pnpqrsystem.scanner.QRCodeScannerActivity;
import com.program.pnpqrsystem.session.SessionManager;
import com.program.pnpqrsystem.user.LoginActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView buildingName = (TextView) findViewById(R.id.textViewBldg);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            buildingName.setText(extras.getString("buildingName").toUpperCase());
        }

    }

    public void scan(View view) {
        Intent qrCodeScannerIntent = new Intent(getApplicationContext(), QRCodeScannerActivity.class);
        qrCodeScannerIntent.putExtra("building", getIntent().getIntExtra("building", 0));
        startActivity(qrCodeScannerIntent);
    }

    public void report(View view) {
        Intent reportSelectionIntent = new Intent(getApplicationContext(), ReportSelectionActivity.class);
        startActivity(reportSelectionIntent);
    }

    public void logout(View view) {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.logoutUser();
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
    }
}