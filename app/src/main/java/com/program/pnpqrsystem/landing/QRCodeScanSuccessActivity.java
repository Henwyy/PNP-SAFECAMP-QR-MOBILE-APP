package com.program.pnpqrsystem.landing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.program.pnpqrsystem.MainActivity;
import com.program.pnpqrsystem.R;
import com.program.pnpqrsystem.scanner.QRCodeScannerActivity;

import java.util.Objects;

public class QRCodeScanSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_success_landing_activity);

        Intent currentIntent = getIntent();
        final String accountId = currentIntent.getStringExtra("accountID");
        final String errors = currentIntent.getStringExtra("errors");

        TextView messageView = findViewById(R.id.message);
        TextView accountIdView = findViewById(R.id.accountIdView);
        if( !accountId.isEmpty() ) {
            messageView.setText("QR Code successfully scanned.");
            accountIdView.setText("Account ID#: " + accountId);
        }else {
            if( Objects.isNull(errors) ) {
                messageView.setText("Account not existing or invalid QR code.");
            }else {
                messageView.setText(errors);
            }
        }
    }

    public void backToMain(View view) {
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivityIntent);
    }

    public void backToScanner(View view) {
        Intent scannerIntent = new Intent(getApplicationContext(), QRCodeScannerActivity.class);
        startActivity(scannerIntent);
    }
}
