package com.example.mcu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class splash_Activity extends AppCompatActivity {
    int SPLASH_SCREEN = 2000;
    //variables
    Animation up_animation, text_animation;
    ImageView splash_logo;
    TextView welcome, to, mcu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        String language = getSharedPreferences("Language", MODE_PRIVATE)
                .getString("lang", "en");
        //Configuration Language
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        boolean isDark = getSharedPreferences("Theme", MODE_PRIVATE)
                .getBoolean("selectedTheme", false);
        if (isDark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //animation
        up_animation = AnimationUtils.loadAnimation(this, R.anim.up_animation);
        text_animation = AnimationUtils.loadAnimation(this, R.anim.up_animation);

        //hooks
        splash_logo = findViewById(R.id.splash_logo);
        welcome = findViewById(R.id.welcome);
        to = findViewById(R.id.to);
        mcu = findViewById(R.id.mcu);

        splash_logo.setAnimation(up_animation);
        welcome.setAnimation(text_animation);
        to.setAnimation(text_animation);
        mcu.setAnimation(text_animation);

        new Handler().postDelayed(this::run, SPLASH_SCREEN);
    }

    private void run() {
        Intent intent = new Intent(splash_Activity.this, login_Activity.class);
        //animation for login screen
        Pair[] Pairs = new Pair[2];
        Pairs[0] = new Pair<View, String>(splash_logo, "logo_image");
        Pairs[1] = new Pair<View, String>(welcome, "logo_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splash_Activity.this, Pairs);
        startActivity(intent, options.toBundle());
        finish();
    }
}