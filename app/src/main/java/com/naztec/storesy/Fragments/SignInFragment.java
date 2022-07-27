package com.naztec.storesy.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.R;

import java.util.Objects;

public class SignInFragment extends Fragment {

    ImageButton btnSignInAsGuest;
    TextInputEditText txtEmail, txtPwd;
    Button btnForgotPassword, btnSignIn, switchToSignUpFrag;

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
        txtEmail = view.findViewById(R.id.sign_in_email);
        txtPwd = view.findViewById(R.id.sign_in_pwd);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        btnForgotPassword = view.findViewById(R.id.sign_in_btn_forgot_password);
        switchToSignUpFrag = view.findViewById(R.id.btn_goto_sign_up_frag);

        btnSignIn.setOnClickListener(v -> {
            String email = Objects.requireNonNull(txtEmail.getText()).toString();
            String pwd = Objects.requireNonNull(txtPwd.getText()).toString();

            if (email.isEmpty())
                txtEmail.setError("Required");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                txtEmail.setError("Enter valid Email");
            else if (pwd.isEmpty())
                txtPwd.setError("Required");
            else if (pwd.length() < 8)
                txtPwd.setError("Password must contain at least 8 characters");
            else
                UserAuthentications.signIn(view.getContext(), email, pwd);
        });

        btnSignInAsGuest.setOnClickListener(v ->
                UserAuthentications.signInAsGuest(view.getContext())
        );

        btnForgotPassword.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().replace(R.id.registration_frame_layout,
                                new ForgotPasswordFragment())
                        .commit()
        );

        switchToSignUpFrag.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().replace(R.id.registration_frame_layout,
                                new SignUpFragment())
                        .commit()
        );

        return view;
    }
}