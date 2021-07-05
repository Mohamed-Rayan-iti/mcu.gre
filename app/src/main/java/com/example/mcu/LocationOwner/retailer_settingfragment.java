package com.example.mcu.LocationOwner;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.mcu.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link retailer_settingfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class retailer_settingfragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;

    // Notification
    NotificationManager notificationManager;
    int NotID = 1;

    // finger
    KeyguardManager keyguardManager;
    FingerprintManager fingerprintManager;

    //
    SwitchMaterial switchLang, switchTheme, switchNotification, switchFingerprint;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public retailer_settingfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment retailer_profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static retailer_settingfragment newInstance(String param1, String param2) {
        retailer_settingfragment fragment = new retailer_settingfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.retailer_settingfragment, container, false);

        notificationManager = (NotificationManager) referenceActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        // Initializing both Android Keyguard Manager and Fingerprint Manager
        keyguardManager = (KeyguardManager) referenceActivity.getSystemService(Context.KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) referenceActivity.getSystemService(Context.FINGERPRINT_SERVICE);


        switchLang = parentHolder.findViewById(R.id.lang_switch);
        switchTheme = parentHolder.findViewById(R.id.theme_switch);
        switchNotification = parentHolder.findViewById(R.id.notifications);
        switchFingerprint = parentHolder.findViewById(R.id.finger_lock);

        // SharedPreferences to set checked language
        String language = requireActivity().getSharedPreferences("Language", MODE_PRIVATE)
                .getString("lang", "en");
        switchLang.setChecked(language.equals("ar"));

        // On Checked Change switch language
        switchLang.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                switchLanguage("ar");
            else
                switchLanguage("en");
        });

        
        // SharedPreferences to set checked theme
        boolean isDarkMode = requireActivity().getSharedPreferences("Theme", MODE_PRIVATE)
                .getBoolean("selectedTheme", false);
        switchTheme.setChecked(isDarkMode);

        // On Checked Change switch Themes
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> switchThemes(isChecked));
        
        

        // SharedPreferences to set checked notification
        boolean isNotificationAllowed = requireActivity().getSharedPreferences("Notification", MODE_PRIVATE)
                .getBoolean("NotificationAllowed", false);
        switchNotification.setChecked(isNotificationAllowed);

        // On Checked Change switch Notification
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> allowNotifications(isChecked));


        // SharedPreferences to set checked fingerprint lock
        boolean isFingerprintAllowed = requireActivity().getSharedPreferences("Fingerprint", MODE_PRIVATE)
                .getBoolean("FingerprintAllowed", false);
        switchFingerprint.setChecked(isFingerprintAllowed);

        // On Checked Change switch Fingerprint
        switchFingerprint.setOnCheckedChangeListener((buttonView, isChecked) -> allowFingerprint(isChecked));

        return parentHolder;
    }

    private void switchThemes(boolean theme) {
        requireActivity().getSharedPreferences("Theme", MODE_PRIVATE)
                .edit()
                .putBoolean("selectedTheme", theme)
                .apply();

        if (theme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Intent intent = new Intent(retailer_settingfragment.this.getActivity(), retailer_dashboard_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void switchLanguage(String language) {
        requireActivity().getSharedPreferences("Language", MODE_PRIVATE)
                .edit()
                .putString("lang", language)
                .apply();

        //Configuration Language
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        Intent intent = new Intent(retailer_settingfragment.this.getActivity(), retailer_dashboard_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    //creates a notification for lollipop with a popup/heads up message..
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void allowNotifications(Boolean isAllow) {
        requireActivity().getSharedPreferences("Notification", MODE_PRIVATE)
                .edit()
                .putBoolean("NotificationAllowed", isAllow)
                .apply();
        
        if (isAllow) {

            boolean isCreatedChannel = requireActivity().getSharedPreferences("isCreatedChannel", MODE_PRIVATE)
                    .getBoolean("createdChannel", false);

            if (!isCreatedChannel) {
                createChannel();
            }
            
            showNotification(getString(R.string.allowed_notification),referenceActivity);
            
        }
    }

    public void showNotification(String description, Activity activity) {
        Notification lollipopNotification = new NotificationCompat.Builder(activity, "test_channel_03")
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.login_logo)
                .setWhen(System.currentTimeMillis())  //When the event occurred, now, since lollipopNotification are stored by time.
                .setContentTitle("MCU Application")   //Title message top row.
                .setContentText(description)  //message when looking at the notification, second row
                //the following 2 lines cause it to show up as popup message at the top in android 5 systems.
                .setPriority(Notification.PRIORITY_MAX)  //could also be PRIORITY_HIGH.  needed for LOLLIPOP, M and N.  But not Oreo
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})  //for the heads/pop up must have sound or vibrate
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)  //VISIBILITY_PRIVATE or VISIBILITY_SECRET
                .setAutoCancel(true)   //allow auto cancel when pressed.
                .setChannelId("test_channel_03")  //Oreo notifications
                .build();  //finally build and return a Notification.

        //Show the notification
        notificationManager.notify(NotID, lollipopNotification);
        NotID--;
    }

    private void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("test_channel_03",
                    getString(R.string.channel_name3),  //name of the channel
                    NotificationManager.IMPORTANCE_HIGH);   //importance level
            // Configure the notification channel.
            mChannel.setDescription(getString(R.string.channel_description3));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.BLACK);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);

            requireActivity().getSharedPreferences("isCreatedChannel", MODE_PRIVATE)
                    .edit()
                    .putBoolean("createdChannel", true)
                    .apply();
        }
    }

    public void allowFingerprint(Boolean isAllow) {
        if (isAllow) {
            if (!fingerprintManager.isHardwareDetected()) {
                isAllow = false;
                switchFingerprint.setChecked(false);
                showAlert(getString(R.string.your_device_does_not_have_a_fingerprint_sensor));
            } else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(retailer_settingfragment.this.referenceActivity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    isAllow = false;
                    switchFingerprint.setChecked(false);
                    showAlert(getString(R.string.fingerprint_authentication_permission_not_enabled));
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        isAllow = false;
                        switchFingerprint.setChecked(false);
                        showAlert(getString(R.string.register_at_least_one_fingerprint_in_settings));
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            isAllow = false;
                            switchFingerprint.setChecked(false);
                            showAlert(getString(R.string.lock_screen_security_not_enabled_in_settings));
                        }
                    }
                }
            }
        }
        requireActivity().getSharedPreferences("Fingerprint", MODE_PRIVATE)
                .edit()
                .putBoolean("FingerprintAllowed", isAllow)
                .apply();
    }

    void showAlert(String msg) {
        new AlertDialog.Builder(retailer_settingfragment.this.referenceActivity)
                .setTitle(R.string.attention)
                .setMessage(msg)
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }

}