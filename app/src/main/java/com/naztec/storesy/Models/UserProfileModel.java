package com.naztec.storesy.Models;

public class UserProfileModel {

    String firstName = "", lastName = "";
    String email = "", UID = "";

    /**
     * To check if the user the user is logged in or not
     *
     * @implSpec if user isn't Signed In then False else True
     * @implNote Value interpreted from performAuth(), signInAsGuest(), signUp()
     */
    boolean isAuthenticated = false;

    /**
     * To check if the user is logged in as Guest or not
     *
     * @implSpec if user is Signed In using Email & Password then True else False
     * @implNote Value interpreted from performAuth(), signInAsGuest(), signUp()
     */
    boolean isVerified = false;

    public UserProfileModel() {
        // Required Empty Constructor
    }

    public UserProfileModel(String UID, String email, boolean isAuthenticated) {
        this.UID = UID;
        this.email = email;
        this.isAuthenticated = isAuthenticated;
    }

    public UserProfileModel(String UID, boolean isAuthenticated) {
        this.UID = UID;
        this.isAuthenticated = isAuthenticated;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUID() {
        return UID;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

}
