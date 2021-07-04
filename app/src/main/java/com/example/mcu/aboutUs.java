package com.example.mcu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.mcu.LocationOwner.*;

public class aboutUs extends AppCompatActivity {
    ImageView iconBack;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_aboutus );
        iconBack = findViewById(R.id.about_us_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, retailer_dashboard_Activity.class);
            startActivity(intent);
        });
    }
}