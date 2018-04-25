package edu.gatech.cs2340.shelterme.model;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.Random;

import edu.gatech.cs2340.shelterme.util.GeneratePassword;

import static android.support.v4.content.ContextCompat.startActivity;

public class UnlockRequest extends Message {
    private User lockedUser;
//    private String lockedUserEmail;

    public UnlockRequest(Account sender) {
        super("User " + sender.getUsername() + " has requested for their account to be" +
                " unlocked.", sender);
        assert(lockedUser != null);
        this.lockedUser = (User)sender;
//        lockedUserEmail = lockedUser.getEmail();
        accountUnlockRequested = true;
    }

    public UnlockRequest() {
        super();
        this.lockedUser = new User();
    }


    public User getLockedUser() {
        return lockedUser;
    }

    public void setLockedUser(User user) {
        this.lockedUser = user;
    }
//
//    public void setLockedUserEmail(String lockedUserEmail) {
//        this.lockedUserEmail = lockedUserEmail;
//    }

    @Override
    public Intent resolve() {
        Log.e("UNLOCK REQUEST", "Subclass 'resolve()' called");
        Random rand = new Random();
        int maxLen = 10;
        int minLen = 5;
        int length = rand.nextInt((maxLen - minLen) + 1) + minLen;
        String newPass = GeneratePassword.randomPassword(length);
        lockedUser.setPassword(newPass.hashCode());
//        lockedUser.setAccountLocked(false);
        Model.updateUserAccountStatus(lockedUser, false);
        return emailNewPassword(newPass);
    }

    private Intent emailNewPassword(String newPass) {
        String deliveryText = "The new password for " + lockedUser.getUsername() + " is " + newPass
                + "\n\nEnter this for your ShelterMe login to access your unlocked account!";
        String[] destination = {lockedUser.getEmail()};

        // send email
//        Intent mail = new Intent(Intent.ACTION_SENDTO);
//        mail.setType("message/rfc822");
        Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        mail.putExtra(Intent.EXTRA_EMAIL, destination);
        mail.putExtra(Intent.EXTRA_SUBJECT, "ShelterMe Password Recovery Service");
        mail.putExtra(Intent.EXTRA_TEXT, deliveryText);

        return mail;
//        return (Intent.createChooser(mail, "Send email..."));
    }
}
