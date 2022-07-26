package com.naztec.storesy.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.R;

public class SignInFragment extends Fragment {

    TextView switchToSignUpFrag;
    ImageButton btnSignInAsGuest;

    public SignInFragment() {
        // Required empty public constructor
    }

    @SuppressLint("PrivateResource")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        btnSignInAsGuest = view.findViewById(R.id.img_btn_sign_in_guest);
        switchToSignUpFrag = view.findViewById(R.id.txt_btn_sign_up_frag);

        btnSignInAsGuest.setOnClickListener(v ->
                UserAuthentications.signInAsGuest(view.getContext())
        );

        switchToSignUpFrag.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().replace(R.id.registration_frame_layout,
                                new SignUpFragment())
                        .commit()
        );

        return view;
    }
}