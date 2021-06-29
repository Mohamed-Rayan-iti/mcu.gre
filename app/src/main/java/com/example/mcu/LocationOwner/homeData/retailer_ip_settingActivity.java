package com.example.mcu.LocationOwner.homeData;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcu.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class retailer_ip_settingActivity extends AppCompatActivity {

    EditText time, timeLift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_ip_setting);
        String ip_setting = getIntent().getStringExtra("ip");
        Toast.makeText(this, ip_setting, Toast.LENGTH_SHORT).show();

        time = findViewById(R.id.time);
        timeLift = findViewById(R.id.timeleft);

    }

    public void startTimeCountDown(View view) {
        Integer timeMillisecond = Integer.parseInt(time.getText().toString()) * 3600000;
        // Time is in millisecond so 50sec = 50000 I have used
        // countdown Interveal is 1sec = 1000 I have used
        new CountDownTimer(timeMillisecond, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timeLift.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                timeLift.setText("00:00:00");
            }
        }.start();
    }
}