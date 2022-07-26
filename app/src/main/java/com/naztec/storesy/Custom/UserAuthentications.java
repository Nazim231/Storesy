package com.naztec.storesy.Custom;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthentications {

    /**
     * To check if the user the user is logged in or not
     *
     * @implSpec if user isn't Signed In then False else True
     * @implNote Value interpreted from performAuth()
     */
    public static boolean isAuthenticated = false;
    /**
     * To check if the user is logged in as Guest or not
     *
     * @implSpec if user is Signed In using Email & Password then True else False
     * @implNote Value interpreted from performAuth()
     */
    public static boolean isVerified = false;

    // Check user logged in or not and is Guest or EmailVerified
    public static void performAuth() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            isAuthenticated = true;
            // User is Guest or Email Verified
            isVerified = currentUser.isEmailVerified();
            if (isVerified) fetchUserProfileData();
        } else {
            isAuthenticated = false;
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

}
