package com.program.pnpqrsystem.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.program.pnpqrsystem.R;

public class ReportSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_selection);
    }

    public void selectReportCivilian(View view) {
        Intent reportIntent = new Intent(getApplicationContext(), ReportCivilianActivity.class);
        startActivity(reportIntent);
    }

    public void selectReportIncident(View view) {
        Intent reportIntent = new Intent(getApplicationContext(), ReportIncidentActivity.class);
        startActivity(reportIntent);
    }
}