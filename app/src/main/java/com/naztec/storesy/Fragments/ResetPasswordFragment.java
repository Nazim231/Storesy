package com.naztec.storesy.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.naztec.storesy.Custom.UserAuthentications;
import com.naztec.storesy.R;

import java.util.Objects;

public class ResetPasswordFragment extends Fragment {

    TextInputEditText txtEmail;
    LinearLayout layoutMailSent;
    Button btnRecoverPassword, btnGoToSignInFrag;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        txtEmail = view.findViewById(R.id.forgot_pwd_email);
        layoutMailSent = view.findViewById(R.id.layout_reset_pwd_mail_sent);
        btnRecoverPassword = view.findViewById(R.id.btn_recover_pwd);
        btnRecoverPassword.setOnClickListener(v -> {
            String email = Objects.requireNonNull(txtEmail.getText()).toString();

            if (email.isEmpty())
                txtEmail.setError("Required");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                txtEmail.setError("Enter Valid Email");
            else {
                btnRecoverPassword.setEnabled(false);
                UserAuthentications.resetPassword(v, email, mailSent -> {
                    if (mailSent) {
                        btnRecoverPassword.setVisibility(View.GONE);
                        //layoutMailSent.setVisibility(View.VISIBLE);
                    } else {
                        btnRecoverPassword.setEnabled(true);
                    }
                });
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