package com.program.pnpqrsystem.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.program.pnpqrsystem.MainActivity;
import com.program.pnpqrsystem.R;

public class ReportSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submission);
    }

    public void backToHome(View view) {
        Intent backToHome = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backToHome);
    }
}