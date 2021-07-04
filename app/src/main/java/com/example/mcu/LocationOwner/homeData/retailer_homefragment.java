package com.example.mcu.LocationOwner.homeData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mcu.Ip.And.Ordernum.model.ipandordermodel;
import com.example.mcu.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class retailer_homefragment extends Fragment {
    private ProgressBar progressBar;
    private homeAdepter adepter;
    private List<ipandordermodel> list;
    private FirebaseFirestore fireStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.retailer_homefragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //hooks
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_home);
        progressBar = view.findViewById(R.id.progressbar_home);
        fireStore = FirebaseFirestore.getInstance();

        // to array data
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        adepter = new homeAdepter(list, getActivity());
        recyclerView.setAdapter(adepter);
        getData();
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        fireStore.collection("IP and order number")
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error == null) {
                        if (value == null) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "value is null !  ", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                ipandordermodel model = documentChange.getDocument().toObject(ipandordermodel.class);
                                list.add(model);
                                adepter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.e("error", error.toString());
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}