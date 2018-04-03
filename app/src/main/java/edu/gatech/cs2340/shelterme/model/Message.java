package edu.gatech.cs2340.shelterme.model;

import java.util.Calendar;

/**
 * Created by austincondict on 2/21/18.
 */

public class Message {
    private String message;
    private String timeSent;
    private String senderEmail;
    private boolean isAddressed;

    public Message(String message, Account sender) {
        this.message = message;
        this.timeSent = Calendar.getInstance().getTime().toString();
        this.senderEmail = sender.getEmail();
        this.isAddressed = false;
    }

    public Message() {
        this("No Message Given", new User());
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public boolean isAddressed() {
        return isAddressed;
    }

    public void setAddressed(boolean addressed) {
        isAddressed = addressed;
    }

    public void address() {
        isAddressed = true;
    }

    @Override
    public String toString() {
        return senderEmail + "\n" + timeSent;
    }
}
