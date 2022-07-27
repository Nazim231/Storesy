package com.naztec.storesy.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.R;

import java.util.Objects;

public class ForgotPasswordFragment extends Fragment {

    TextInputEditText txtEmail;
    Button btnRecoverPassword, btnGoToSignInFrag;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        txtEmail = view.findViewById(R.id.forgot_pwd_email);
        btnRecoverPassword = view.findViewById(R.id.btn_recover_pwd);
        btnRecoverPassword.setOnClickListener(v -> {
            String email = Objects.requireNonNull(txtEmail.getText()).toString();

            if (email.isEmpty())
                txtEmail.setError("Required");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                txtEmail.setError("Enter Valid Email");
            else {
                UserAuthentications.resetPassword(v, email);
            }
        });

        btnGoToSignInFrag = view.findViewById(R.id.btn_goto_sign_in_frag);
        btnGoToSignInFrag.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().replace(R.id.registration_frame_layout,
                                new SignInFragment())
                        .commit()
        );

        return view;
    }
}