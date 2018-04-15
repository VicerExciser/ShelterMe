package edu.gatech.cs2340.shelterme.model;

/**
 * Represents a request for reporting a user.
 * Created by austincondict on 2/21/18.
 */

public class ReportUserRequest extends Message {
    private String reportedUserEmail;

    /**
     * A constructor for ReportUserRequest class
     * @param reportedUser the user being reported
     * @param message the message of the email
     * @param sender the sender of the email
     */
    public ReportUserRequest(User reportedUser, String message, Employee sender) {
        super(message, sender);
        reportedUserEmail = reportedUser.getEmail();
    }

    /**
     * A no-arg constructor for ReportUserRequest class
     */
    public ReportUserRequest() {
        super();
        reportedUserEmail = new User().getEmail();
    }

    /**
     * A getter for reportedUserEmail
     * @return the email of the reported user
     */
    public String getReportedUserEmail() {
        return reportedUserEmail;
    }

    /**
     * A setter for the email of the reported user
     * @param reportedUserEmail the email of the reported user
     */
    public void setReportedUserEmail(String reportedUserEmail) {
        this.reportedUserEmail = reportedUserEmail;
    }
}
