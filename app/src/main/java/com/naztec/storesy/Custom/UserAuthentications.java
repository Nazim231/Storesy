package com.naztec.storesy.Custom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.naztec.storesy.MainActivity;
import com.naztec.storesy.UserRegistrationActivity;

import java.util.HashMap;
import java.util.Objects;

public class UserAuthentications {

    /**
     * User Information
     */
    // Values interpreted from signUp(), clearUserInfo()
    public static String userFirstName = "";
    public static String userLastName = "";
    // Value interpreted from signInAsGuest(), signUp(), clearUserInfo()
    public static String UID = "";

    /**
     * To check if the user the user is logged in or not
     *
     * @implSpec if user isn't Signed In then False else True
     * @implNote Value interpreted from performAuth(), signInAsGuest(), signUp()
     */
    public static boolean isAuthenticated = false;
    /**
     * To check if the user is logged in as Guest or not
     *
     * @implSpec if user is Signed In using Email & Password then True else False
     * @implNote Value interpreted from performAuth(), signInAsGuest(), signUp()
     */
    public static boolean isVerified = false;


    /**
     * To check user logged in or not and is Guest or EmailVerified
     *
     * @implNote Invoked from SplashActivity
     */
    public static void performAuth() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            isAuthenticated = true;
            // User is Guest or Email Verified
            isVerified = currentUser.isEmailVerified();
            if (isVerified) fetchUserProfileData();
        } else {
            isAuthenticated = false;
            isVerified = false;
        }

    }

    /**
     * Fetch Full User Profile
     *
     * @implNote Invoked from performAuth()
     */
    public static void fetchUserProfileData() {
        // TODO : Fetch User Details like UID, Email Address, Delivery Addresses, Orders, Ratings, Wishlist, Cart, etc.
    }

    /**
     * Signing In User using Guest Account
     *
     * @param context to make the use of Context whenever needed
     * @implNote Invoked from SignInFragment & SignUpFragment on btnSignInAsGuest
     */
    public static void signInAsGuest(Context context) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Signing in as Guest");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        fAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        isAuthenticated = true;
                        assert fAuth.getCurrentUser() != null;
                        UID = fAuth.getCurrentUser().getUid();
                        pd.dismiss();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } else {
                        isAuthenticated = false;
                        pd.dismiss();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                        builder.setTitle("Error")
                                .setMessage(Objects.requireNonNull(task.getException()).getMessage())
                                .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                                }))
                                .setPositiveButton("Retry", ((dialogInterface, i) ->
                                        signInAsGuest(context)
                                ));
                        Dialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
    }

    /**
     * Creating User Account using Email & Password
     * TODO : Add functionality to Verify the User Email using link or OTP
     *
     * @param context   to make the use of Context whenever needed
     * @param firstName Users' First Name
     * @param lastName  Users' Last Name
     * @param email     Users' Email
     * @param pwd       Users' Password
     * @implNote Invoked from SignUpFragment on btnSignUp
     */
    public static void signUp(Context context, String firstName, String lastName, String email, String pwd) {

        ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Creating User Account...");
        pd.setCanceledOnTouchOutside(false);
        pd.create();
        pd.show();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Account Created
                        isAuthenticated = true;
                        isVerified = true;

                        // Adding User Info to Database
                        assert firebaseAuth.getCurrentUser() != null;
                        UID = firebaseAuth.getCurrentUser().getUid();
                        HashMap<Object, String> data = new HashMap<>();
                        data.put("firstName", firstName);
                        data.put("lastName", lastName);
                        db.collection("USERS").document(UID).set(data, SetOptions.merge())
                                .addOnCompleteListener(dataInsertion -> {
                                    if (dataInsertion.isSuccessful()) {
                                        // Profile Created
                                        userFirstName = firstName;
                                        userLastName = lastName;
                                        pd.dismiss();
                                        context.startActivity(new Intent(context, MainActivity.class));
                                        ((Activity) context).finish();
                                    } else {
                                        // Profile Creation Failed
                                        pd.dismiss();
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                                        builder.setTitle("Profile Creation Failed")
                                                .setMessage(Objects.requireNonNull(task.getException()).getMessage())
                                                .setNegativeButton("OK", ((dialogInterface, i) -> {
                                                }));
                                        Dialog dialog = builder.create();
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.show();
                                    }
                                });

                    } else {
                        // Failed to Create Account
                        isAuthenticated = false;
                        isVerified = false;
                        pd.dismiss();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                        builder.setTitle("Error")
                                .setMessage(Objects.requireNonNull(task.getException()).getMessage())
                                .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                                }))
                                .setPositiveButton("Retry", ((dialogInterface, i) ->
                                        signUp(context, firstName, lastName, email, pwd)
                                ));
                        Dialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });
    }

    public static void signOut(Context context) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        clearUserInfo();
        context.startActivity(new Intent(context, UserRegistrationActivity.class));
        ((Activity) context).finish();
    }

    private static void clearUserInfo() {
        UID = "";
        userFirstName = "";
        userLastName = "";

    }

}
