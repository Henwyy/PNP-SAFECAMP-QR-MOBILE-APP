package com.program.pnpqrsystem.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.program.pnpqrsystem.R;
import com.program.pnpqrsystem.settings.Environment;

import java.util.HashMap;
import java.util.Map;

public class ReportIncidentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
    }

    public void submit(View view) {
        EditText subjectText = findViewById(R.id.subjectIncidentText);
        EditText descriptionText = findViewById(R.id.descriptionIncidentText);

        sendIncidentReportDetails( subjectText.getText().toString(), descriptionText.getText().toString() );
    }

    private void sendIncidentReportDetails(  final String subject, final String description ) {
        String postUrl = Environment.ROOT_PATH + "/api/v1/addIncidentReport";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ReportIncidentActivity", response);
                        startActivity(new Intent(getApplicationContext(), ReportSubmissionActivity.class));
                    }
                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("subject", subject);
                params.put("description", description);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}