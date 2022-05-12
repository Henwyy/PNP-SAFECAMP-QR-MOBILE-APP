package com.program.pnpqrsystem.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.program.pnpqrsystem.R;
import com.program.pnpqrsystem.landing.QRCodeScanSuccessActivity;
import com.program.pnpqrsystem.session.SessionManager;
import com.program.pnpqrsystem.settings.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QRCodeScannerActivity extends AppCompatActivity {
    private CodeScanner codeScanner;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_scanner_activity);

        sessionManager = new SessionManager(getApplicationContext());

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
//            Toast.makeText(QRCodeScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

            final String url =  Environment.ROOT_PATH + "/api/v1/getUser?qrCodeString=" + result.getText();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    jsonArray -> {
                        final String accountId = checkifAccountExists(jsonArray);
                        if( !accountId.isEmpty() ) {
                            if( "1".equals(sessionManager.getUserDetails().get(SessionManager.KEY_BUILDING)) ) {
                                sendAccountForTimeInMainGate(accountId);
                            }else {
                                sendAccountForTime(accountId);
                            }
                        }else {
                            // TODO no account or invalid qr
                        }
                    }, volleyError -> {
                        volleyError.printStackTrace();
                        Toast.makeText(QRCodeScannerActivity.this, "Error fetching account", Toast.LENGTH_SHORT).show();
                    });

            requestQueue.add(arrayRequest);


        }));

        codeScanner.startPreview();
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });


    }

    private String checkifAccountExists( JSONArray jsonArray ) {
        try {
            if (jsonArray.length() > 0) {
                JSONObject accountObject = jsonArray.getJSONObject(0);
                final String accountID = accountObject.getString("account_id");
                return accountID;
//                Intent landingPageIntent = new Intent(getApplicationContext(), QRCodeScanSuccessActivity.class);
//                landingPageIntent.putExtra("accountID", accountID);
//                startActivity(landingPageIntent);
            } else {
                // No account or invalid qr code
                // Toast.makeText(QRCodeScannerActivity.this, "Non-existing account or Invalid QR Code", Toast.LENGTH_SHORT).show();
                Intent landingPageIntent = new Intent(getApplicationContext(), QRCodeScanSuccessActivity.class);
                landingPageIntent.putExtra("accountID", "");
                startActivity(landingPageIntent);
            }
        } catch (JSONException e) {
            Toast.makeText(QRCodeScannerActivity.this, "Parse Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return "";
    }

    private void sendAccountForTime( String accountID ) {
        Log.i("sendAccountForTime", sessionManager.getUserDetails().get("username"));
        String postUrl = Environment.ROOT_PATH + "/api/v1/addTime";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.i("BUILDING", getIntent().getIntExtra("building", 0)+"");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        Intent landingPageIntent = new Intent(getApplicationContext(), QRCodeScanSuccessActivity.class);

                        if( responseObject.getBoolean("success") ) {
                            landingPageIntent.putExtra("accountID", accountID);
                        }else {
                            landingPageIntent.putExtra("accountID", "");
                            landingPageIntent.putExtra("errors", responseObject.getString("errors"));
                        }

                        startActivity(landingPageIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("account_id",accountID);
                params.put("building", sessionManager.getUserDetails().get("building"));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void sendAccountForTimeInMainGate( String accountID ) {
        Log.i("sendAccountForTimeInMainGate", sessionManager.getUserDetails().get("username"));
        String postUrl = Environment.ROOT_PATH + "/api/v1/addTimeInMainGate";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.i("sendAccountForTimeInMainGate", "IN sendAccountForTimeInMainGate");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        Intent landingPageIntent = new Intent(getApplicationContext(), QRCodeScanSuccessActivity.class);

                        if( responseObject.getBoolean("success") ) {
                            landingPageIntent.putExtra("accountID", accountID);
                        }else {
                            landingPageIntent.putExtra("accountID", "");
                            landingPageIntent.putExtra("errors", responseObject.getString("errors"));
                        }

                        startActivity(landingPageIntent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("account_id",accountID);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
//        codeScanner.startPreview();
        Log.i("OnPause", "On paused");
    }

}
