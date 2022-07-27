package com.naztec.storesy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naztec.storesy.Custom.UserAuthentications;

public class EmailVerificationActivity extends AppCompatActivity {

    LinearLayout layoutVerifyEmail, layoutWaitingForVerification;
    Button btnSendVerificationEmail;
    TextView userEmail;

    FirebaseUser currentUser;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        layoutVerifyEmail = findViewById(R.id.layout_verify_email);
        layoutWaitingForVerification = findViewById(R.id.layout_waiting_for_email_verification);

        userEmail = findViewById(R.id.verification_email_address);
        userEmail.setText(currentUser.getEmail());

        btnSendVerificationEmail = findViewById(R.id.btn_verify_email);
        btnSendVerificationEmail.setOnClickListener(view -> {
            layoutVerifyEmail.setVisibility(View.GONE);
            layoutWaitingForVerification.setVisibility(View.VISIBLE);
            UserAuthentications.sendVerificationEmail(view);
        });

    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = () -> {
            handler.postDelayed(runnable, delay);
            if (UserAuthentications.isEmailVerified())
            {
                handler.removeCallbacks(runnable);
                // Add Profile Data to the Database and go to MainActivity
                UserAuthentications.addProfileDataToDB(this, dataUploaded -> {
                    if (dataUploaded) {
                        Toast.makeText(this, "Email Verified Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();
                    }
                });
                UserAuthentications.userData.setVerified(true);
            }
        }, delay);
        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }
}