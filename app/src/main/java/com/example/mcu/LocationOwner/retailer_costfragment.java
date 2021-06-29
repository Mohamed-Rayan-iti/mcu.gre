   package com.example.mcu.LocationOwner;

import android.app.Activity;
import android.os.Bundle;

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
    EditText num1 , num2 , res;


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

        calc = (Button) parentHolder.findViewById(R.id.btn_cost);
        num1 = (EditText) parentHolder.findViewById(R.id.total_hours);
        num2 = (EditText) parentHolder.findViewById(R.id.price_per_hour);
        res = (EditText) parentHolder.findViewById(R.id.total_cost);


        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Number1=Integer.parseInt(num1.getText().toString());
                int Number2=Integer.parseInt(num2.getText().toString());
                int result=Number1* Number2;
                res.setText(String.valueOf(result));
            }
        });



        return parentHolder;
    }


}