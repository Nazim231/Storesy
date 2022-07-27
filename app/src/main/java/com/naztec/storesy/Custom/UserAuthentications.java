package com.naztec.storesy.Custom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.naztec.storesy.MainActivity;
import com.naztec.storesy.Models.UserProfileModel;
import com.naztec.storesy.UserRegistrationActivity;

import java.util.HashMap;
import java.util.Objects;

public class UserAuthentications {

    /**
     * User Information
     *
     * @implNote Data interpreted from signUp(), signInAsGuest(), signIn(),
     * clearUserInfo(), fetchUserProfileData(), performAuth()
     * @implSpec String{firstName, lastName, email, UID}, boolean{isAuthenticated, isVerified}
     */
    public static UserProfileModel userData = new UserProfileModel();


    /**
     * To check user logged in or not and is Guest or EmailVerified
     *
     * @implNote Invoked from SplashActivity, signIn()
     */
    public static void performAuth(Context context) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (!currentUser.isAnonymous()) {
                // User is Signed In with Email
                fetchUserProfileData(context);
            } else {
                // User is Guest
                userData = new UserProfileModel(currentUser.getUid(), true);
            }
        } else {
            // No Account Signed In
            clearUserInfo();
        }
    }

    /**
     * Fetch Full User Profile
     *
     * @implNote Invoked from performAuth()
     */
    public static void fetchUserProfileData(Context context) {
        // TODO : Fetch User Details like UID, Email Address, Delivery Addresses, Orders, Ratings, Wishlist, Cart, etc.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert currentUser != null;
        userData = new UserProfileModel(currentUser.getUid(), currentUser.getEmail(), true, true);

        // Fetching User Details from DB
        db.collection("USERS").document(userData.getUID()).get(Source.SERVER)
                .addOnSuccessListener(documentSnapshot -> {
                    userData.setFirstName(documentSnapshot.getString("firstName"));
                    userData.setLastName(documentSnapshot.getString("lastName"));
                })
                .addOnFailureListener(e -> {
                    Log.e("PROFILE_DATA_FAILURE:", e.getMessage());
                    Toast.makeText(context, "Error : PROFILE_DATA_FAILURE", Toast.LENGTH_SHORT).show();
                });

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
                        assert fAuth.getCurrentUser() != null;
                        userData = new UserProfileModel(fAuth.getCurrentUser().getUid(), true);
                        pd.dismiss();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } else {
                        clearUserInfo();
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

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        fAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Account Created
                        assert fAuth.getCurrentUser() != null;
                        userData = new UserProfileModel(fAuth.getCurrentUser().getUid(), email, true, true);
                        // Adding User Info to Database
                        HashMap<Object, String> data = new HashMap<>();
                        data.put("firstName", firstName);
                        data.put("lastName", lastName);
                        db.collection("USERS").document(userData.getUID()).set(data, SetOptions.merge())
                                .addOnCompleteListener(dataInsertion -> {
                                    if (dataInsertion.isSuccessful()) {
                                        // Profile Created
                                        userData.setFirstName(firstName);
                                        userData.setLastName(lastName);
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
                        clearUserInfo();
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

    /**
     * Signing In User using Email & Password
     *
     * @param context to make the use of Context whenever needed
     * @param email   Users' Email
     * @param pwd     Users' Password
     * @implNote Invoked from SignInFragment on btnSignIn
     */
    public static void signIn(Context context, String email, String pwd) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Signing In...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Signed In
                        pd.dismiss();
                        performAuth(context);  // To Fetch User Information

                        context.startActivity(new Intent(context, MainActivity.class));
                        ((Activity) context).finish();
                    } else {
                        pd.dismiss();
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                        builder.setTitle("Sign In Failed")
                                .setMessage(error)
                                .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                                }))
                                .setPositiveButton("Retry", ((dialogInterface, i) ->
                                        signIn(context, email, pwd)
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

    // To clear whole User Information
    private static void clearUserInfo() {
        userData = new UserProfileModel();

    }

}
