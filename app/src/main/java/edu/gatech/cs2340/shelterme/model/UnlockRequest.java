package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/21/18.
 */

public class UnlockRequest extends Message {
    private String lockedUserEmail;

    /**
     * A constructor for UnlockRequest class
     * @param lockedUser the locked user
     * @param message the message of the email
     * @param sender the sender of the email
     */
    public UnlockRequest(User lockedUser, String message, Account sender) {
        super(message, sender);
        this.lockedUserEmail = lockedUser.getEmail();
    }

    /**
     * A no-arg constructor for UnlockRequest
     */
    public UnlockRequest() {
        super();
        this.lockedUserEmail = new User().getEmail();
    }

    /**
     * A getter for lockedUserEmail
     * @return the email of the locked user
     */
    public String getLockedUserEmail() {
        return lockedUserEmail;
    }

    /**
     * A setter of the email of the locked user
     * @param lockedUserEmail the email of the locked user
     */
    public void setLockedUserEmail(String lockedUserEmail) {
        this.lockedUserEmail = lockedUserEmail;
    }
}
