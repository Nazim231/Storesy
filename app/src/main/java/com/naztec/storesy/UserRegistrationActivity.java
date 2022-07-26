package com.naztec.storesy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.naztec.storesy.Fragments.SignInFragment;

public class UserRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        getSupportFragmentManager().beginTransaction().replace(R.id.registration_frame_layout, new SignInFragment())
                .commit();

    }
}