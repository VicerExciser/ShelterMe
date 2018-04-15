package edu.gatech.cs2340.shelterme.model;


class ReportUserRequest extends Message {

    public ReportUserRequest(User reportedUser, String message, Employee sender) {
        super(message, sender);
        @SuppressWarnings("unused") String reportedUserEmail = reportedUser.getEmail();
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
