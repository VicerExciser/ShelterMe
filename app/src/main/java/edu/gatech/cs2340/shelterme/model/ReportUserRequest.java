package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/21/18.
 */

class ReportUserRequest extends Message {
    private String reportedUserEmail;

    public ReportUserRequest(User reportedUser, String message, Employee sender) {
        super(message, sender);
        reportedUserEmail = reportedUser.getEmail();
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public ReportUserRequest() {
//        super();
//        reportedUserEmail = new User().getEmail();
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public String getReportedUserEmail() {
//        return reportedUserEmail;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setReportedUserEmail(String reportedUserEmail) {
//        this.reportedUserEmail = reportedUserEmail;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
}
