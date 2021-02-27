package com.example.to_do_app_final.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.to_do_app_final.Activity.MainActivity;
import com.example.to_do_app_final.Activity.NameOfNotesHolder;
import com.example.to_do_app_final.R;

public class Notes extends Fragment {
    TextView newData;
    CardView notesData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        notesData = view.findViewById(R.id.notes);
        notesData(view);
        return view;
    }

    private void notesData(View view) {
        notesData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}