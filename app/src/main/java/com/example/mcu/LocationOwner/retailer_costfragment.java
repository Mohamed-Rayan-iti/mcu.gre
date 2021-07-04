package com.example.mcu.LocationOwner;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mcu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link retailer_costfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class retailer_costfragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    Button calc;
    EditText totalHours, pricePerHour, totalCost;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public retailer_costfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment retailer_manageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static retailer_costfragment newInstance(String param1, String param2) {
        retailer_costfragment fragment = new retailer_costfragment();
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
        // Inflate the layout for this fragment
        parentHolder = inflater.inflate(R.layout.retailer_costfragment, container, false);
        calc = parentHolder.findViewById(R.id.btn_cost);
        totalHours = parentHolder.findViewById(R.id.total_hours);
        pricePerHour = parentHolder.findViewById(R.id.price_per_hour);
        totalCost = parentHolder.findViewById(R.id.total_cost);
        calc.setOnClickListener(view -> inputValid());
        return parentHolder;
    }

    private void inputValid() {
        String inputTotalHours = totalHours.getText().toString();
        String inputPricePerHour = pricePerHour.getText().toString();
        if (inputTotalHours.isEmpty()) {
            // Alert
            showAlert(getString(R.string.total_hours_mustnt_be_empty));
            return;
        }
        if (inputPricePerHour.isEmpty()) {
            // Alert
            showAlert(getString(R.string.price_per_hour_mustnt_be_empty));
            return;
        }
        calcTotalCost(Integer.parseInt(inputTotalHours), Integer.parseInt(inputPricePerHour));
    }

    private void calcTotalCost(int inputTotalHours, int inputPricePerHour) {
        totalCost.setText(String.valueOf(inputTotalHours * inputPricePerHour));
    }

    void showAlert(String msg) {
        new AlertDialog.Builder(retailer_costfragment.this.referenceActivity)
                .setTitle(R.string.attention)
                .setMessage(msg)
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }
}