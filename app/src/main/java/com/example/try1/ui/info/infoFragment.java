package com.example.try1.ui.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.try1.R;
import com.example.try1.ui.ViewPager.ViewPagerActivity;


//10120069 Rendy Agustin IF2//

public class infoFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        Button myButton = view.findViewById(R.id.details);

        // Set a click listener for the button
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform an action when the button is clicked
                // For example, start a new activity
                Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}