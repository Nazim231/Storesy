package com.naztec.storesy.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.R;

public class AccountFragment extends Fragment {

    Button btnSignOut;
    TextView firstName, lastName, uid, auth, verified, email;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        firstName = view.findViewById(R.id.user_first_name);
        lastName = view.findViewById(R.id.user_last_name);
        uid = view.findViewById(R.id.user_uid);
        auth = view.findViewById(R.id.user_auth);
        verified = view.findViewById(R.id.user_verified);
        email = view.findViewById(R.id.user_email);

        firstName.setText(UserAuthentications.userData.getFirstName());
        lastName.setText(UserAuthentications.userData.getLastName());
        uid.setText(UserAuthentications.userData.getUID());
        auth.setText((UserAuthentications.userData.isAuthenticated()) ? "True" : "False");
        verified.setText((UserAuthentications.userData.isVerified()) ? "True" : "False");
        email.setText(UserAuthentications.userData.getEmail());


        btnSignOut = view.findViewById(R.id.btn_sign_out);
        btnSignOut.setOnClickListener(v -> UserAuthentications.signOut(view.getContext()));

        return view;
    }
}