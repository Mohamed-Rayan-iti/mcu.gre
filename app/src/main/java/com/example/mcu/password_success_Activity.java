package com.example.mcu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class password_success_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_success);
        findViewById(R.id.go_login).setOnClickListener(v -> {
            Intent intent = new Intent(this, login_Activity.class);
            startActivity(intent);
            finish();
        });
    }
}