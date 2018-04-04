package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/21/18.
 */

public class UnlockRequest extends Message {
    private String lockedUserEmail;

    public UnlockRequest(User lockedUser, String message, Account sender) {
        super(message, sender);
        this.lockedUserEmail = lockedUser.getEmail();
    }

    public UnlockRequest() {
        super();
        this.lockedUserEmail = new User().getEmail();
    }


    public String getLockedUserEmail() {
        return lockedUserEmail;
    }

    public void setLockedUserEmail(String lockedUserEmail) {
        this.lockedUserEmail = lockedUserEmail;
    }
}
