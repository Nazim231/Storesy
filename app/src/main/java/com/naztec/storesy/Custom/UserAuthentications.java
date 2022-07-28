package com.naztec.storesy.Custom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.naztec.storesy.EmailVerificationActivity;
import com.naztec.storesy.MainActivity;
import com.naztec.storesy.Models.UserProfileModel;
import com.naztec.storesy.UserRegistrationActivity;

import java.util.HashMap;
import java.util.Objects;

public class UserAuthentications {

    /**
     * User Information
     *
     * @implNote Data interpreted from signUp(), signInAsGuest(),
     * clearUserInfo(), fetchUserProfileData(), performAuth()
     * @implSpec String{firstName, lastName, email, UID}, boolean{isAuthenticated, isVerified}
     */
    public static UserProfileModel userData = new UserProfileModel();


    /**
     * To check user logged in or not and is Guest or EmailVerified
     *
     * @implNote Invoked from SplashActivity
     */
    public static void performAuth(Context context) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(() -> {
            if (currentUser != null) {
                currentUser.reload();
                if (!currentUser.isAnonymous()) {
                    // User is Signed In with Email
                    if (currentUser.isEmailVerified()) {
                        fetchUserProfileData(context, true);
                    } else {
                        userData = new UserProfileModel(currentUser.getUid(), currentUser.getEmail(), true);
                        context.startActivity(new Intent(context, EmailVerificationActivity.class));
                        ((Activity) context).finish();
                    }
                } else {
                    // User is Guest
                    userData = new UserProfileModel(currentUser.getUid(), true);
                    context.startActivity(new Intent(context, MainActivity.class));
                    ((Activity) context).finish();
                }
            } else {
                // No Account Signed In
                clearUserInfo();
                context.startActivity(new Intent(context, UserRegistrationActivity.class));
                ((Activity) context).finish();
            }

        }, 2000);
    }

    /**
     * Fetch Full User Profile
     *
     * @param context  to make the use of Context whenever needed
     * @param goToMain if True then redirect to MainActivity
     * @implNote Invoked from performAuth(), signIn()
     */
    public static void fetchUserProfileData(Context context, boolean goToMain) {
        // TODO : Fetch User Details like UID, Email Address, Delivery Addresses, Orders, Ratings, Wishlist, Cart, etc.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert currentUser != null;
        userData = new UserProfileModel(currentUser.getUid(), currentUser.getEmail(), true);
        userData.setVerified(true);
        // Fetching User Details from DB
        db.collection("USERS").document(userData.getUID()).get(Source.SERVER)
                .addOnSuccessListener(documentSnapshot -> {
                    userData.setFirstName(documentSnapshot.getString("firstName"));
                    userData.setLastName(documentSnapshot.getString("lastName"));
                    if (goToMain) {
                        context.startActivity(new Intent(context, MainActivity.class));
                        ((Activity) context).finish();
                    }
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
     *
     * @param view      to make the use of Context whenever needed
     * @param firstName Users' First Name
     * @param lastName  Users' Last Name
     * @param email     Users' Email
     * @param pwd       Users' Password
     * @implNote Invoked from SignUpFragment on btnSignUp
     */
    public static void signUp(View view, String firstName, String lastName, String email, String pwd) {

        ProgressDialog pd = new ProgressDialog(view.getContext());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Creating User Account...");
        pd.setCanceledOnTouchOutside(false);
        pd.create();
        pd.show();

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        fAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pd.dismiss();
                        //Account Created
                        assert fAuth.getCurrentUser() != null;
                        userData = new UserProfileModel(fAuth.getCurrentUser().getUid(), email, true);
                        userData.setFirstName(firstName);
                        userData.setLastName(lastName);
                        // Verifying Email
                        view.getContext().startActivity(new Intent(view.getContext(), EmailVerificationActivity.class));
                        ((Activity) view.getContext()).finish();
                    } else {
                        // Failed to Create Account
                        clearUserInfo();
                        pd.dismiss();
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(view.getContext());
                        builder.setTitle("Error")
                                .setMessage(Objects.requireNonNull(task.getException()).getMessage())
                                .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                                }))
                                .setPositiveButton("Retry", ((dialogInterface, i) ->
                                        signUp(view, firstName, lastName, email, pwd)
                                ));
                        Dialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });
    }

    /**
     * To Verify Users' Entered Email
     *
     * @param view to make the use of View whenever needed
     * @implNote Invoked from EmailVerificationActivity on btnSendVerificationEmail
     */
    public static void sendVerificationEmail(View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        currentUser.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar.make(view, "Email Sent, Please check your Inbox",
                                Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(view, "An error occurred, please try again",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Check if the Email is Verified or not
     *
     * @return boolean value
     * @implSpec Invoked from EmailVerificationActivity via onResume()
     */
    public static boolean isEmailVerified() {
        boolean verified = false;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            verified = currentUser.isEmailVerified();
        }
        return verified;
    }

    /**
     * Interface to see if the Given Task is successfully completed or not
     * using interface because FirebaseQuery can't return any value
     *
     * @implSpec used in addProfileDataToDB(), resetPassword()
     */
    public interface TaskResult {
        void isTaskSuccessful(boolean taskResult);
    }

    /**
     * To Upload the User Profile Data to Database after Email Verification
     *
     * @param context    to make the use of Context whenever needed
     * @param taskResult interface to check either data is uploaded or not
     * @implSpec Invoked from EmailVerificationActivity via onResume() after Email Verified
     */
    public static void addProfileDataToDB(Context context, TaskResult taskResult) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Data Map
        HashMap<Object, String> data = new HashMap<>();
        data.put("firstName", userData.getFirstName());
        data.put("lastName", userData.getLastName());

        /* TODO : Take User FirstName and LastName if account is not verified at the time of account
                 creation
        */
        db.collection("USERS").document(userData.getUID()).set(data, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        taskResult.isTaskSuccessful(true);
                    } else {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                        builder.setTitle("Profile Creation Failed");
                        builder.setMessage(Objects.requireNonNull(task.getException()).getMessage());
                        builder.setPositiveButton("Retry", ((dialogInterface, i) -> addProfileDataToDB(context, taskResult)));
                        builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
                            context.startActivity(new Intent(context, MainActivity.class));
                            ((Activity) context).finish();
                        }));

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
                        assert fAuth.getCurrentUser() != null;
                        fAuth.getCurrentUser().reload();
                        pd.dismiss();
                        // Checking if User Email is Verified or Not
                        if (fAuth.getCurrentUser().isEmailVerified()) {
                            fetchUserProfileData(context, true);
                        } else {
                            // If User Email isn't Verified he must verify email before continuing
                            context.startActivity(new Intent(context, EmailVerificationActivity.class));
                            ((Activity) context).finish();
                        }
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

    /**
     * To Reset User Password
     *
     * @param view  to make the use of View whenever needed
     * @param email User Email whose password needs to be changed
     * @param mailSent Interface to return True when mail is sent else False
     * @implSpec Invoked from ResetPasswordFragment on btnRecoverPassword
     */
    public static void resetPassword(View view, String email, TaskResult mailSent) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        fAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar.make(view, "Email Sent, Check your Inbox to Reset Password",
                                Snackbar.LENGTH_SHORT).show();
                        mailSent.isTaskSuccessful(true);
                    } else {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(view.getContext());
                        builder.setTitle(":(")
                                .setMessage(Objects.requireNonNull(task.getException()).getMessage())
                                .setNegativeButton("OK", ((dialogInterface, i) -> {
                                }))
                                .create().show();
                        mailSent.isTaskSuccessful(false);
                    }
                });
    }

    // Sign Out User and Clear all User Profile Data
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
