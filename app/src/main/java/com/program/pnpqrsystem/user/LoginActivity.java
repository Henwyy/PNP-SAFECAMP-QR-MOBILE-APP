package com.program.pnpqrsystem.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.program.pnpqrsystem.MainActivity;
import com.program.pnpqrsystem.R;
import com.program.pnpqrsystem.session.SessionManager;
import com.program.pnpqrsystem.settings.Environment;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText loginText = findViewById(R.id.loginText);
        EditText passwordText = findViewById(R.id.passwordText);
        errorText = findViewById(R.id.errorLoginTextView);
        validateLogin( loginText.getText().toString(), passwordText.getText().toString() );
    }

    private void validateLogin(final String username, final String password) {
        String postUrl = Environment.ROOT_PATH + "/api/v1/validate-officer-login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                response -> {
                    Log.i("LoginResponse", response);
                    try {
                        JSONObject loginResponse = new JSONObject(response);
                        Log.i("building", loginResponse.getInt("building")+"");

                        if( loginResponse.getBoolean("success") ) {
                            errorText.setText("");
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.createLoginSession( username, loginResponse.getInt("building") );

                            Intent home = new Intent(getApplicationContext(), MainActivity.class);
                            home.putExtra("building", loginResponse.getInt("building"));
                            home.putExtra("buildingName", username);
                            startActivity(home);


                        }else {
                            errorText.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                        Intent landingPageIntent = new Intent(getApplicationContext(), QRCodeScanSuccessActivity.class);
//                        Log.i("@@ accountID: ", accountID);
//                        landingPageIntent.putExtra("accountID", accountID);
//                        startActivity(landingPageIntent);
//                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", DigestUtils.sha1Hex(password));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}