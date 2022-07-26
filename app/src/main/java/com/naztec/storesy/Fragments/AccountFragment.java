package com.naztec.storesy.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.R;

public class AccountFragment extends Fragment {

    Button btnSignOut;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        btnSignOut = view.findViewById(R.id.btn_sign_out);
        btnSignOut.setOnClickListener(v -> UserAuthentications.signOut(view.getContext()));

        return view;
    }
}