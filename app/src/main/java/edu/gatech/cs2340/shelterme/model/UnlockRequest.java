package edu.gatech.cs2340.shelterme.model;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.Random;

import edu.gatech.cs2340.shelterme.util.GeneratePassword;

/**
 * The type Unlock request.
 */
public class UnlockRequest extends Message {
    private Account lockedUser;
//    private String lockedUserEmail;

    /**
     * Instantiates a new Unlock request.
     *
     * @param sender the sender
     */
    public UnlockRequest(Account sender) {
        super("User " + sender.getUsername() + " has requested for their account to be" +
                " unlocked.", sender);
        this.lockedUser = sender;
//        lockedUserEmail = lockedUser.getEmail();
        accountUnlockRequested = true;
    }

    /**
     * Instantiates a new Unlock request.
     */
    public UnlockRequest() {
        super();
        this.lockedUser = new User();
    }


    /**
     * Gets locked user.
     *
     * @return the locked user
     */
    public Account getLockedUser() {
        return lockedUser;
    }

    /**
     * Sets locked user.
     *
     * @param user the user
     */
    public void setLockedUser(Account user) {
        this.lockedUser = user;
    }

    @Override
    public Intent resolve() {
        Log.e("UNLOCK REQUEST", "Unlock Request resolved");
        Random rand = new Random();
        int maxLen = 10;
        int minLen = 5;
        int length = rand.nextInt((maxLen - minLen) + 1) + minLen;
        String newPass = GeneratePassword.randomPassword(length);
        lockedUser.setPassword(newPass.hashCode());
        this.markAsAddressed();
        // This doubles to unlock the account and update the database
//        lockedUser.setAccountLocked(false);
        lockedUser.unlockAccount();
        return emailNewPassword(newPass);
    }

    private Intent emailNewPassword(String newPass) {
        String deliveryText = "The new password for " + lockedUser.getUsername() + " is " + newPass
                + "\n\nEnter this for your ShelterMe login to access your unlocked account!";
        String[] destination = {lockedUser.getEmail()};

        // send email
        Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        mail.putExtra(Intent.EXTRA_EMAIL, destination);
        mail.putExtra(Intent.EXTRA_SUBJECT, "ShelterMe Password Recovery Service");
        mail.putExtra(Intent.EXTRA_TEXT, deliveryText);

        return mail;
    }
}
