package com.example.mcu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.mcu.LocationOwner.retailer_dashboard_Activity;

import static androidx.core.content.ContextCompat.getColor;


/**
 * Created by whit3hawks on 11/16/16.
 */
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private final Context context;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
        Intent intent = new Intent(this.context, retailer_dashboard_Activity.class);
        this.context.startActivity(intent);
    }

    public void update(String e, Boolean success) {
        TextView textView = ((Activity) context).findViewById(R.id.errorText);
        textView.setText(e);
        if (success) {
            textView.setTextColor(getColor(context, R.color.white));
        }
    }
}