package com.example.mcu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class sign_up_Activity extends AppCompatActivity {


    // variables

    EditText password, confirm_password, phone_number, e_mail, id;
    Button sign_up_btn, back_login;
    private ProgressBar progressBar;
    // firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;


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
        firestore = FirebaseFirestore.getInstance();


        sign_up_btn.setOnClickListener(v ->
                validationData());
        // link from sign_up_Activity to login_Activity

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up_Activity.this, login_Activity.class);
                sign_up_Activity.this.startActivity(intent);

            }
        });

    }


    private void validationData() {

        String email = e_mail.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String conpass = confirm_password.getText().toString().trim();
        String phone = phone_number.getText().toString().trim();
        String idmanger = id.getText().toString().trim();


        //trust data
        // email
        if (email.isEmpty()) {
            e_mail.requestFocus();
            //Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.email_is_required));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e_mail.requestFocus();
            // Toast.makeText(this, "Invalid Email address \nEmail must be like example@company.com", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.Email_must_be_like)+"\n"+"example@company.com");
            return;
        }
        // password
        if (pass.isEmpty()) {
            password.requestFocus();
            // first option
            //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.password_is_required));
            return;

        }

        if (pass.length() < 8) {
            password.requestFocus();
            //Toast.makeText(this, "Password must be 8 digits", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.password_must_be_8_digits));
            return;
        }

        if (conpass.isEmpty()) {
            confirm_password.requestFocus();
            // Toast.makeText(this, "Confirm Password is required", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.confirm_password_is_required));
            return;
        }

        if (!pass.equals(conpass)) {
            confirm_password.requestFocus();
            //Toast.makeText(this, "Password not like Confirm Password !", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.password_not_like_confirm_password));
            return;

        }


        // phone
        if (phone.isEmpty()) {
            phone_number.requestFocus();
            // Toast.makeText(this, "Phone is required", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.phone_is_required));
            return;
        }

        if (!phone.matches("^([0])([1])([0,1,2,5])([0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])")) {
            phone_number.requestFocus();
            //Toast.makeText(this, "Invalid Phone Number \nmust be like 01*********", Toast.LENGTH_SHORT).show();

            // change to Alert
            showAlert(getString(R.string.phone_number_must_be_11_digits));
            return;
        }

        // id manager
        if (idmanger.isEmpty()) {
            id.requestFocus();
            //Toast.makeText(this, "ID is required", Toast.LENGTH_SHORT).show();

            // change to Alert
            showAlert(getString(R.string.id_is_required));
            return;
        }

        if (idmanger.length() < 14) {
            id.requestFocus();
            //Toast.makeText(this, "Invalid ID ", Toast.LENGTH_SHORT).show();


            // change to Alert
            showAlert(getString(R.string.invalid_id));
            return;
        }
        // Toast.makeText(this, "valid", Toast.LENGTH_SHORT).show();
        // to sign up from fire base
        signupwithfirebase(email, pass);

    }

    private void signupwithfirebase(String email, String pass) {

        progressBar.setVisibility(View.VISIBLE);


        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            saveUserData();

                        } else {
                            progressBar.setVisibility(View.GONE);

                            showAlert(task.getException().getMessage());
                        }
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


            firestore.collection("Users")
                    .document(userID)
                    .set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                new AlertDialog.Builder(sign_up_Activity.this)
                                        .setTitle("congratulation")
                                        .setMessage("Account created Successful")
                                        .setCancelable(false)
                                        .setIcon(R.drawable.ic_done)
                                        .setPositiveButton("Okay!",
                                                (dialog, which) -> {

                                                    startActivity(new Intent(sign_up_Activity.this, login_Activity.class));

                                                })
                                        .create().show();

                            } else {
                                progressBar.setVisibility(View.GONE);

                                showAlert("Error \n " + task.getException().getMessage());
                            }

                        }

                        @NotNull
                        private OnCompleteListener<Void> getOnCompleteListener() {
                            return this;
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

