package com.example.mcu.LocationOwner.homeData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mcu.Ip.And.Ordernum.model.control_ip_model;
import com.example.mcu.Ip.And.Ordernum.model.ipandordermodel;
import com.example.mcu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class retailer_ip_settingActivity extends AppCompatActivity {

    EditText time, timeLift, speed;
    Button btnStart;
    ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_ip_setting);
        String ip_setting = getIntent().getStringExtra("ip");
        String id = getIntent().getStringExtra("id");
        String order = getIntent().getStringExtra("order");
//        Toast.makeText(this, ip_setting, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(this, "Please Enter Your Desire Time", Toast.LENGTH_LONG).show();
                } else if (speed.getText().toString().equals("")) {
                    Toast.makeText(this, "Please Enter Your Desire Speed", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    int Order = Integer.parseInt(order);
                    int Speed = Integer.parseInt(speed.getText().toString());
                    int Time = Integer.parseInt(time.getText().toString());

                    try {
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//dd/MM/yyyy
                        Date now = new Date();
                        String startDate = sdfDate.format(now);
                        Log.e("startDate", startDate);
                        Date strDtae = sdfDate.parse(startDate);

                        Calendar time = Calendar.getInstance();
                        time.setTime(strDtae);
                        time.add(Calendar.HOUR, Time);

                        String end_date = sdfDate.format(time.getTime());
                        Log.e("time_after_Break", end_date);
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
        long timeMillisecond = difference;
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

    public void startTimeCountDown() {
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


    public void setControl(String docmentId, String ip, int order, int speed, int hour, String start_time, String end_time) {

//        List<control_ip_model> control_list = new ArrayList<>();
        control_ip_model control_model = new control_ip_model();

        control_model.setEndTime(end_time);
        control_model.setHour(hour);
        control_model.setId(docmentId);
        control_model.setIp(ip);
        control_model.setOrder(order);
        control_model.setStartTime(start_time);
        control_model.setSpeed(speed);

//        control_list.add(control_model);

        db.collection("control").document(docmentId).set(control_model).addOnSuccessListener(unused -> {
            progressBar.setVisibility(View.GONE);
            startTimeCountDown();

        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Log.e("failure", e.toString());
        });
    }

    public void getControlSettings(String id) {

        db.collection("control").document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                progressBar.setVisibility(View.GONE);

                control_ip_model control_ip_model = documentSnapshot.toObject(control_ip_model.class);
//                Log.e("existing", Objects.requireNonNull(control_ip_model).getIp());
                speed.setText("" + control_ip_model.getSpeed());
                time.setText("" + control_ip_model.getHour());

                try {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    DateFormat calenderFormater = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

                    Date date2 = simpleDateFormat.parse(control_ip_model.getEndTime());
                    Date date1 = calenderFormater.parse(String.valueOf(Calendar.getInstance().getTime()));

                    if (date1.after(date2)) {
                        Toast.makeText(this, "Time Has Finished", Toast.LENGTH_LONG).show();
                    } else {
                        long difference = date2.getTime() - date1.getTime();
                        Log.e("difference", "" + difference);
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

}