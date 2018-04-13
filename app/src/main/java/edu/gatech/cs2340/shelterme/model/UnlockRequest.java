package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/21/18.
 */

class UnlockRequest extends Message {
    private String lockedUserEmail;

    public UnlockRequest(User lockedUser, String message, Account sender) {
        super(message, sender);
        this.lockedUserEmail = lockedUser.getEmail();
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public UnlockRequest() {
//        super();
//        this.lockedUserEmail = new User().getEmail();
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)


// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public String getLockedUserEmail() {
//        return lockedUserEmail;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setLockedUserEmail(String lockedUserEmail) {
//        this.lockedUserEmail = lockedUserEmail;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
}
