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
import com.program.pnpqrsystem.landing.QRCodeScanSuccessActivity;
import com.program.pnpqrsystem.settings.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportCivilianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_civilian);
    }

    public void submit(View view) {
        EditText civilianIdText = findViewById(R.id.civilianIdText);
        EditText subjectText = findViewById(R.id.subjectText);
        EditText descriptionText = findViewById(R.id.descriptionText);

        sendCivilianReportDetails( civilianIdText.getText().toString(), subjectText.getText().toString(), descriptionText.getText().toString() );
    }

    private void sendCivilianReportDetails( final String civilianID, final String subject, final String description ) {
        String postUrl = Environment.ROOT_PATH + "/api/v1/addCivilianReport";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ReportCivilianActivity", response);
                        startActivity(new Intent(getApplicationContext(), ReportSubmissionActivity.class));
                    }
                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("civilian_id", civilianID);
                params.put("subject", subject);
                params.put("description", description);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}