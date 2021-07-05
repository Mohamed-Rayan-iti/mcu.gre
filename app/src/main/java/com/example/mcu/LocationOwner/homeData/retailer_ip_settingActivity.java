package com.example.mcu.LocationOwner.homeData;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mcu.Ip.And.Ordernum.model.control_ip_model;
import com.example.mcu.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class retailer_ip_settingActivity extends AppCompatActivity {
    EditText time, timeLift, speed;
    Button btnStart;
    ProgressBar progressBar;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_ip_setting);
        String ip_setting = getIntent().getStringExtra("ip");
        String id = getIntent().getStringExtra("id");
        String order = getIntent().getStringExtra("order");
        time = findViewById(R.id.time);
        timeLift = findViewById(R.id.timeleft);
        speed = findViewById(R.id.speed);
        btnStart = findViewById(R.id.btn_start);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        getControlSettings(id);
        try {
            btnStart.setOnClickListener(v -> {
                if (time.getText().toString().equals("")) {
                    showAlert(getString(R.string.please_enter_your_desire_time));
                } else if (speed.getText().toString().equals("")) {
                    showAlert(getString(R.string.please_enter_your_desire_speed));
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    int Order = Integer.parseInt(order);
                    int Speed = Integer.parseInt(speed.getText().toString());
                    int Time = Integer.parseInt(time.getText().toString());
                    try {
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//dd/MM/yyyy
                        Date now = new Date();
                        String startDate = sdfDate.format(now);
                        Date strDate = sdfDate.parse(startDate);
                        Calendar time = Calendar.getInstance();
                        assert strDate != null;
                        time.setTime(strDate);
                        time.add(Calendar.HOUR, Time);
                        String end_date = sdfDate.format(time.getTime());
                        setControl(id, ip_setting, Order, Speed, Time, startDate, end_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            Log.e("exception", ex.toString());
        }
    }

    public void startTimeCountDown(long difference) {
        // Time is in millisecond so 50sec = 50000 I have used
        // countdown Interval is 1sec = 1000 I have used
        new CountDownTimer(difference, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timeLift.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            @SuppressLint("SetTextI18n")
            public void onFinish() {
                timeLift.setText("00:00:00");
            }
        }.start();
    }

    public void startTimeCountDown() {
        Integer timeMillisecond = Integer.parseInt(time.getText().toString()) * 3600000;
        // Time is in millisecond so 50sec = 50000 I have used
        // countdown Interval is 1sec = 1000 I have used
        new CountDownTimer(timeMillisecond, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timeLift.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            // When the task is over it will print 00:00:00 there
            @SuppressLint("SetTextI18n")
            public void onFinish() {
                timeLift.setText("00:00:00");
            }
        }.start();
    }


    public void setControl(String documentId, String ip, int order, int speed, int hour, String start_time, String end_time) {
        control_ip_model control_model = new control_ip_model();
        control_model.setEndTime(end_time);
        control_model.setHour(hour);
        control_model.setId(documentId);
        control_model.setIp(ip);
        control_model.setOrder(order);
        control_model.setStartTime(start_time);
        control_model.setSpeed(speed);

        db.collection("control").document(documentId).set(control_model).addOnSuccessListener(unused -> {
            progressBar.setVisibility(View.GONE);
            startTimeCountDown();
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Log.e("failure", e.toString());
        });
    }

    @SuppressLint("SetTextI18n")
    public void getControlSettings(String id) {
        db.collection("control").document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                progressBar.setVisibility(View.GONE);
                control_ip_model control_ip_model = documentSnapshot.toObject(control_ip_model.class);
                assert control_ip_model != null;
                speed.setText("" + control_ip_model.getSpeed());
                time.setText("" + control_ip_model.getHour());
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    DateFormat calenderFormatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
                    Date date2 = simpleDateFormat.parse(control_ip_model.getEndTime());
                    Date date1 = calenderFormatter.parse(String.valueOf(Calendar.getInstance().getTime()));
                    assert date1 != null;
                    if (date1.after(date2)) {
                        Toast.makeText(this, "Time Has Finished", Toast.LENGTH_LONG).show();
                    } else {
                        assert date2 != null;
                        long difference = date2.getTime() - date1.getTime();
                        startTimeCountDown(difference);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                Log.e("noControl", "There is no control");
            }
        }).addOnFailureListener(e -> Log.e("getControlFailure", e.toString()));
    }

    void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.attention)
                .setMessage(msg)
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }
}