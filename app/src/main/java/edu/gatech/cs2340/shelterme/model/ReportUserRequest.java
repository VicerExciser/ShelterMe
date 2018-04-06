package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/21/18.
 */

public class ReportUserRequest extends Message {
    private String reportedUserEmail;

    public ReportUserRequest(User reportedUser, String message, Employee sender) {
        super(message, sender);
        reportedUserEmail = reportedUser.getEmail();
    }

    public ReportUserRequest() {
        super();
        reportedUserEmail = new User().getEmail();
    }

    public String getReportedUserEmail() {
        return reportedUserEmail;
    }

    public void setReportedUserEmail(String reportedUserEmail) {
        this.reportedUserEmail = reportedUserEmail;
    }
}
