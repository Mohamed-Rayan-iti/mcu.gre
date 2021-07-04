package com.example.mcu.LocationOwner;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.mcu.LocationOwner.homeData.retailer_homefragment;
import com.example.mcu.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Locale;
import java.util.Objects;

public class retailer_dashboard_Activity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_dashboard);

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

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new retailer_homefragment()).commit();
        bottomMenu();
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(i -> {
            Fragment fragment = null;
            switch (i) {
                case R.id.bottom_nav_home:
                    fragment = new retailer_homefragment();
                    break;
                case R.id.bottom_nav_cost:
                    fragment = new retailer_costfragment();
                    break;
                case R.id.bottom_nav_Setting:
                    fragment = new retailer_settingfragment();
                    break;
                case R.id.bottom_nav_profile:
                    fragment = new retailer_profilefragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Objects.requireNonNull(fragment)).commit();

        });
    }
}
