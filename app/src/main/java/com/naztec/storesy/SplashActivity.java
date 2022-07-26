package com.naztec.storesy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.naztec.storesy.Custom.UserAuthentications;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Performing Authentication
        UserAuthentications.performAuth();

        new Handler().postDelayed(() -> {
            Intent intent;
            if (UserAuthentications.isAuthenticated)
                intent = new Intent(this, MainActivity.class);
            else
                intent = new Intent(this, UserRegistrationActivity.class);
            startActivity(intent);
            this.finish();
        }, 2000);
    }
}