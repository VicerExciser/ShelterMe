package edu.gatech.cs2340.shelterme.model;


import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * The type Report user request.
 */
public class ReportUserRequest extends Message {
    private User reportedUser;

    /**
     * Instantiates a new Report user request.
     *
     * @param sender       the sender
     * @param reportedUser the reported user
     */
    public ReportUserRequest(Account sender, User reportedUser) {
        super(String.format("%s %s has reported user [%s] for a violation of the ShelterMe Terms " +
                "of Service", sender.getAccountType(), sender.getUsername(),
                reportedUser.getUsername()), sender);
        this.reportedUser = reportedUser;
//        String reportedUserEmail = reportedUser.getEmail();
        accountUnlockRequested = false;
    }

    /**
     * Instantiates a new Report user request.
     */
    public ReportUserRequest() {
        super();
        this.reportedUser = new User();
    }

    /**
     * Gets reported user.
     *
     * @return the reported user
     */
// Can be implemented in the future for AccountDetails page to profile a User
    // All Account details are attached to a Message and thus visible on the Firebase console already
    public User getReportedUser() {
        return reportedUser;
    }

    /**
     * Sets reported user.
     *
     * @param reportedUser the reported user
     */
    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    @Override
    public Intent resolve() {
        Log.e("USER REPORT", "User has been banned");
//        reportedUser.setAccountLocked(true);
        reportedUser.banAccount();
        this.reportedUser.setAccountLocked(true);
        this.markAsAddressed();
        return emailBanNotice();
    }

    private Intent emailBanNotice() {
        String deliveryText = "Your account has been flagged for a violation of the ShelterMe " +
                "Terms of Service and is currently locked while under review.\n\nYou may send " +
                "an administrator request to appeal your ban by selecting 'I forgot my password' " +
                "at the Login screen.\nYour password will be reset in the process.";
        String[] destination = { reportedUser.getEmail()} ;

        // send email
        Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        mail.putExtra(Intent.EXTRA_EMAIL, destination);
        mail.putExtra(Intent.EXTRA_SUBJECT, "ShelterMe Account Suspension");
        mail.putExtra(Intent.EXTRA_TEXT, deliveryText);

        return mail;
    }
}
