package com.example.mcu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class forgetpassword_Activity extends AppCompatActivity {

    String email;
    int confirmCode;
    ImageView iconBack;

    FirebaseAuth firebaseAuth;
    private TextInputLayout activation_forget_layout;
    private EditText e_mail, activation_code;
    private Button btnSend, btnConfirm, btnEditEmail, btnResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword_);

        firebaseAuth = FirebaseAuth.getInstance();

        e_mail = findViewById(R.id.e_mail_forget);
        activation_forget_layout=findViewById(R.id.activation_forget_layout);
        activation_code = findViewById(R.id.activation_forget);

        btnSend = findViewById(R.id.btn_sent);
        btnConfirm = findViewById(R.id.btn_activation);
        btnEditEmail = findViewById(R.id.btn_edit_email);
        btnResend = findViewById(R.id.btn_resent);


        iconBack = findViewById(R.id.forget_password_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(forgetpassword_Activity.this, login_Activity.class);
            startActivity(intent);
        });
    }


    private int createRandomCode() {
        Random random = new Random();
        int confirmEmailCode = random.nextInt(999999 - 100001 + 1) + 100001;
        confirmCode = confirmEmailCode;
        return confirmCode;
    }

    public void sentEmailCode() {
        new Thread(() -> {
            try {
                final String username;
                username = "mcu.mis.2021@gmail.com";
                final String password;
                password = "MIS.MCU.2021";
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session session;
                session = Session.getInstance(properties,
                        new Authenticator() {

                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("MCU.App@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(email));
                    message.setSubject("MCU Application - Activation Code");
                    message.setText(" Dear : " + email + " \n Activation code is : " + createRandomCode() + "");
                    Transport.send(message);

                } catch (MessagingException mex) {
                    throw new RuntimeException(mex);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();

        Toast.makeText(forgetpassword_Activity.this, "Activation code has been sent \n check your email", Toast.LENGTH_LONG).show();

    }

    public void btnSendEmailCode(View view) {
        if (validate()) {
            firebaseAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        boolean isNewUser = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty();
                        if (isNewUser) {
                            showAlert("this email is not exist");
                        } else {
                            sentEmailCode();
                            btnSend.setVisibility(View.INVISIBLE);
                            btnEditEmail.setVisibility(View.VISIBLE);
                            btnResend.setVisibility(View.VISIBLE);
                            btnConfirm.setVisibility(View.VISIBLE);
                            activation_forget_layout.setVisibility(View.VISIBLE);
                            e_mail.setEnabled(false);
                        }

                    });
        }
    }

    void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("attention!")
                .setMessage(msg)
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton("Okay!", null)
                .create().show();
    }

    private boolean validate() {
        email = e_mail.getText().toString().trim();
            if (email.isEmpty()) {
                e_mail.requestFocus();
                // change to Alert
                showAlert("Email is required");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                e_mail.requestFocus();
                // change to Alert
                showAlert("Invalid Email address \nEmail must be like example@company.com");
                return false;
            } else {
                return true;
            }
    }


    public void btnEditEmail(View view) {
        btnSend.setVisibility(View.VISIBLE);
        btnEditEmail.setVisibility(View.INVISIBLE);
        btnResend.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        activation_forget_layout.setVisibility(View.INVISIBLE);
        e_mail.setEnabled(true);
    }

    public void btnResendCode(View view) {

        new Thread(() -> {
            try {
                final String username = "mcu.mis.2021@gmail.com";
                final String password = "MIS.MCU.2021";
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session session = Session.getInstance(properties,
                        new javax.mail.Authenticator() {

                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("MCU.App@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(email));
                    message.setSubject("MCU Application - Activation Code");
                    message.setText(" Dear : " + email + " \n Activation code is : " + confirmCode + "");
                    Transport.send(message);


                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();

        Toast.makeText(this, "Activation code has been Resent", Toast.LENGTH_LONG).show();
    }

    public void btnConfirmEmailCode(View view) {
        if (confirmCode == Integer.parseInt(activation_code.getText().toString())) {
            Intent intent = new Intent(this, new_password_Activity.class);
            intent.putExtra("Email", email);
            startActivity(intent);

        } else {
            showAlert("Invalid Activation Code");
        }
    }
}


