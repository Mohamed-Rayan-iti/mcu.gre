package com.example.mcu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcu.LocationOwner.retailer_dashboard_Activity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login_Activity extends AppCompatActivity {
    Button callSignUp, login_btn, forgetPassword;
    ImageView loginLogo;
    TextView welcomeBack, sign_in_to_continue;
    EditText email_login, password_login;
    private CheckBox rememberMe;
    private ProgressBar progressBar;

    //firebase
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //HOOKS
        callSignUp = findViewById(R.id.sign_up_log);
        login_btn = findViewById(R.id.login_btn);
        forgetPassword = findViewById(R.id.forget_password);
        loginLogo = findViewById(R.id.login_logo);
        welcomeBack = findViewById(R.id.welcome_back);
        sign_in_to_continue = findViewById(R.id.sign_in_to_continue);
        email_login = findViewById(R.id.email_log);
        password_login = findViewById(R.id.password_log);
        rememberMe = findViewById(R.id.remember_melogin);
        progressBar = findViewById(R.id.progressbar_login);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // link from login to sign up
        callSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(login_Activity.this, sign_up_Activity.class);

            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<View, String>(loginLogo, "logo_image");
            pairs[1] = new Pair<View, String>(welcomeBack, "logo_text");
            pairs[2] = new Pair<View, String>(sign_in_to_continue, "logo_desc");
            pairs[3] = new Pair<View, String>(email_login, "username_tran");
            pairs[4] = new Pair<View, String>(password_login, "password_tran");
            pairs[5] = new Pair<View, String>(forgetPassword, "forget_tran");
            pairs[6] = new Pair<View, String>(login_btn, "button_tran");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login_Activity.this, pairs);
            login_Activity.this.startActivity(intent, options.toBundle());
        });
        {
            // link from login to forget password
            forgetPassword.setOnClickListener(v -> {
                Intent intent = new Intent(login_Activity.this, forgetpassword_Activity.class);
                login_Activity.this.startActivity(intent);
            });
        }

        //onclick to login
        findViewById(R.id.login_btn).setOnClickListener(v -> login_Activity.this.validationData());
    }

    private void validationData() {
        String emailLogin = email_login.getText().toString().trim();
        String passLogin = password_login.getText().toString().trim();

        //trust data
        // email name
        if (emailLogin.isEmpty()) {
            email_login.requestFocus();
            showAlert(getString(R.string.email_is_required));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailLogin).matches()) {
            email_login.requestFocus();
            showAlert(getString(R.string.invalid_email_address));
            return;
        }
        // password
        if (passLogin.isEmpty()) {
            password_login.requestFocus();
            showAlert(getString(R.string.password_is_required));
            return;

        }
        if (passLogin.length() < 8) {
            password_login.requestFocus();
            showAlert(getString(R.string.password_must_be_8_digits));
            return;
        }
        signInWithfirebase(emailLogin, passLogin);
    }

    private void signInWithfirebase(String emailLogin, String passLogin) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(emailLogin, passLogin)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        if (rememberMe.isChecked()) {
                            getSharedPreferences("Login", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isLogin", true)
                                    .apply();
                        }
                        goToMain();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        showAlert("Error \n " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.attention)
                .setMessage(msg)
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }

    protected void onStart() {
        super.onStart();
        boolean isLogin = getSharedPreferences("Login", MODE_PRIVATE).getBoolean("isLogin", false);
        if (isLogin) {
            boolean isFingerprintAllowed = getSharedPreferences("Fingerprint", MODE_PRIVATE)
                    .getBoolean("FingerprintAllowed", false);
            if (isFingerprintAllowed)
                gotoFingerprintAuth();
            else
                goToMain();
        }
    }

    void gotoFingerprintAuth() {
        startActivity(new Intent(login_Activity.this, FingerprintActivity.class));
        finish();
    }

    void goToMain() {
        startActivity(new Intent(login_Activity.this, retailer_dashboard_Activity.class));
        finish();
    }
}

