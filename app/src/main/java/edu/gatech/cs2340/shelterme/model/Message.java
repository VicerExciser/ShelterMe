package edu.gatech.cs2340.shelterme.model;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by austincondict on 2/21/18.
 */

public class Message implements Comparable<Message> {
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

    @Override
    public int compareTo(@NonNull Message other) {
        long thisTime = new Date(this.getTimeSent()).getTime();
        long otherTime = new Date(other.getTimeSent()).getTime();
        return (int)(otherTime - thisTime);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Message)) return false;
        return (other == this) || (((Message) other).getTimeSent().equals(this.getTimeSent())
                && ((Message) other).getSenderEmail().equalsIgnoreCase(this.getSenderEmail())
                && ((Message) other).getMessage().equalsIgnoreCase(this.getMessage()));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + this.getSenderEmail().toLowerCase().hashCode();
        result = result * 31 + this.getMessage().toLowerCase().hashCode();
        result = result * 31 + this.getTimeSent().toLowerCase().hashCode();
        return result;
    }
}
