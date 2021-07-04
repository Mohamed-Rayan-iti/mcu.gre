package com.example.mcu.LocationOwner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mcu.R;
import com.example.mcu.aboutUs;
import com.example.mcu.splash_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link retailer_profilefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class retailer_profilefragment extends Fragment {
    private TextView username;
    private CircleImageView userImage;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public retailer_profilefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment retailer_profilefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static retailer_profilefragment newInstance(String param1, String param2) {
        retailer_profilefragment fragment = new retailer_profilefragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retailer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.user_name_prof);
        userImage = view.findViewById(R.id.profile_image);
        progressBar = view.findViewById(R.id.progressbar_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        getUserData();

        // link from profile to contact us
        view.findViewById(R.id.btn_contact_us).setOnClickListener(v -> sendmail());

        // link from profile to privacy
        view.findViewById(R.id.btn_privacy).setOnClickListener(v -> openPrivacy());

        // link from profile to about us
        view.findViewById(R.id.btn_about_us).setOnClickListener(v -> {
            Intent intent = new Intent(retailer_profilefragment.this.getActivity(), aboutUs.class);
            startActivity(intent);
        });

        // link from profile to log out
        view.findViewById(R.id.btn_log_out).setOnClickListener(v -> goLogOut());
    }

    private void goLogOut() {
        if (getActivity() != null)
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.logout)
                    .setMessage(R.string.would_you_like_to_logout)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        // logout
                        getActivity().getSharedPreferences("Login", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();

                        firebaseAuth.signOut();
                        startActivity(new Intent(getActivity(), splash_Activity.class));
                        getActivity().finish();
                    })

                    .setNegativeButton(R.string.no, (dialog, which) -> {
                        // user doesn't want to logout
                    })
                    .create().show();
    }

    private void openPrivacy() {
        Uri uri = Uri.parse("https://m-c-u.flycricket.io/privacy.html"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @SuppressLint("IntentReset")
    private void sendmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, "mcu.mis.2021@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void getUserData() {
        if (getActivity() != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                progressBar.setVisibility(View.VISIBLE);
                String uID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference db = fireStore.collection("Users").document(uID);
                db.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        assert snapshot != null;
                        String mailDB = snapshot.getString("E_mail");
                        String imageDB = snapshot.getString("image");

                        username.setText(mailDB);
                        assert imageDB != null;
                        if (!imageDB.equals("null")) {
                            Glide.with(getActivity())
                                    .load(imageDB)
                                    .placeholder(R.drawable.ic_camera)
                                    .into(userImage);
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Error in task \n" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
