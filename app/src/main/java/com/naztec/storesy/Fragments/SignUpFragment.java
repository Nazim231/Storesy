package com.naztec.storesy.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.R;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    TextView switchToSignIn;
    ImageButton btnSignInAsGuest;
    TextInputEditText txtFirstName, txtLastName;
    TextInputEditText txtEmail;
    TextInputEditText txtPwd, txtConfPwd;
    Button btnSignUp;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        btnSignInAsGuest = view.findViewById(R.id.img_btn_sign_in_guest);

        txtFirstName = view.findViewById(R.id.sign_up_first_name);
        txtLastName = view.findViewById(R.id.sign_up_last_name);
        txtEmail = view.findViewById(R.id.sign_up_email);
        txtPwd = view.findViewById(R.id.sign_up_pwd);
        txtConfPwd = view.findViewById(R.id.sign_up_conf_pwd);
        btnSignUp = view.findViewById(R.id.btn_sign_up);

        switchToSignIn = view.findViewById(R.id.btn_goto_sign_in_frag);

        btnSignInAsGuest.setOnClickListener(v ->
                UserAuthentications.signInAsGuest(view.getContext())
        );

        btnSignUp.setOnClickListener(v -> {
            String firstName = Objects.requireNonNull(txtFirstName.getText()).toString();
            String lastName = Objects.requireNonNull(txtLastName.getText()).toString();
            String email = Objects.requireNonNull(txtEmail.getText()).toString();
            String pwd = Objects.requireNonNull(txtPwd.getText()).toString();
            String confPwd = Objects.requireNonNull(txtConfPwd.getText()).toString();

            if (firstName.isEmpty())
                txtFirstName.setError("Required");
            else if (lastName.isEmpty())
                txtLastName.setError("Required");
            else if (email.isEmpty())
                txtEmail.setError("Required");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                txtEmail.setError("Enter Valid Email");
            else if (pwd.isEmpty())
                txtPwd.setError("Required");
            else if (confPwd.isEmpty())
                txtConfPwd.setError("Required");
            else if (!pwd.equals(confPwd))
                txtConfPwd.setError("Password doesn't match");
            else
                UserAuthentications.signUp(v, firstName, lastName, email, pwd);

        });

        switchToSignIn.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().replace(R.id.registration_frame_layout,
                                new SignInFragment())
                        .commit()
        );
        return view;
    }
}