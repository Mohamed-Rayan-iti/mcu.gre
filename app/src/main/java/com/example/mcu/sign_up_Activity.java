package com.example.mcu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class sign_up_Activity extends AppCompatActivity {
    // variables
    EditText password, confirm_password, phone_number, e_mail, id;
    Button sign_up_btn, back_login;
    private ProgressBar progressBar;
    // firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_);
        // hooks
        e_mail = findViewById(R.id.e_mail_sign_up);
        password = findViewById(R.id.password_sign_up);
        confirm_password = findViewById(R.id.confirm_password_sign_up);
        phone_number = findViewById(R.id.phone_number_sign_up);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        back_login = findViewById(R.id.back_to_login);
        id = findViewById(R.id.id_manager_sign_up);
        progressBar = findViewById(R.id.progressbar_signup);
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        sign_up_btn.setOnClickListener(v ->
                validationData());
        // link from sign_up_Activity to login_Activity
        back_login.setOnClickListener(v -> {
            Intent intent = new Intent(sign_up_Activity.this, login_Activity.class);
            sign_up_Activity.this.startActivity(intent);
        });
    }

    private void validationData() {
        String email = e_mail.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirm_password.getText().toString().trim();
        String phone = phone_number.getText().toString().trim();
        String idManger = id.getText().toString().trim();
        //trust data
        // email
        if (email.isEmpty()) {
            e_mail.requestFocus();
            // Alert
            showAlert(getString(R.string.email_is_required));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e_mail.requestFocus();
            // Alert
            showAlert(getString(R.string.Email_must_be_like) + "\n" + "example@company.com");
            return;
        }
        // password
        if (pass.isEmpty()) {
            password.requestFocus();
            // Alert
            showAlert(getString(R.string.password_is_required));
            return;
        }
        if (pass.length() < 8) {
            password.requestFocus();
            // Alert
            showAlert(getString(R.string.password_must_be_8_digits));
            return;
        }
        // confirm password
        if (confirmPass.isEmpty()) {
            confirm_password.requestFocus();
            // Alert
            showAlert(getString(R.string.confirm_password_is_required));
            return;
        }
        if (!pass.equals(confirmPass)) {
            confirm_password.requestFocus();
            // Alert
            showAlert(getString(R.string.password_not_like_confirm_password));
            return;
        }
        // phone
        if (phone.isEmpty()) {
            phone_number.requestFocus();
            // Alert
            showAlert(getString(R.string.phone_is_required));
            return;
        }
        if (!phone.matches("^([0])([1])([0,1,2,5])([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])")) {
            phone_number.requestFocus();
            // Alert
            showAlert(getString(R.string.phone_number_must_be_11_digits));
            return;
        }
        // id manager
        if (idManger.isEmpty()) {
            id.requestFocus();
            // Alert
            showAlert(getString(R.string.id_is_required));
            return;
        }
        if (idManger.length() < 14) {
            id.requestFocus();
            // Alert
            showAlert(getString(R.string.invalid_id));
            return;
        }
        // to sign up from fire base
        signUpWithFirebase(email, pass);
    }

    private void signUpWithFirebase(String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserData();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        showAlert(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    private void saveUserData() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userID = firebaseAuth.getCurrentUser().getUid();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("id", userID);
            user.put("E_mail", e_mail.getText().toString().trim());
            user.put("password", password.getText().toString().trim());
            user.put("confirm_password", confirm_password.getText().toString().trim());
            user.put("phone_number", phone_number.getText().toString().trim());
            user.put("ID", id.getText().toString().trim());
            user.put("image", "null");

            fireStore.collection("Users")
                    .document(userID)
                    .set(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(sign_up_Activity.this)
                                    .setTitle("congratulation")
                                    .setMessage("Account created Successful")
                                    .setCancelable(false)
                                    .setIcon(R.drawable.ic_done)
                                    .setPositiveButton("Okay!",
                                            (dialog, which) -> startActivity(new Intent(sign_up_Activity.this, login_Activity.class)))
                                    .create().show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            showAlert("Error \n " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }
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

