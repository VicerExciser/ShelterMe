package edu.gatech.cs2340.shelterme.model;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class Message implements Comparable<Message> {
    private final String message;
    private final String timeSent;
    private final String senderEmail;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean isAddressed;

    public Message(String message, Account sender) {
        this.message = message;
        //noinspection ChainedMethodCall
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

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setMessage(String message) {
//        this.message = message;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public String getTimeSent() {
        return timeSent;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setSenderEmail(String senderEmail) {
//        this.senderEmail = senderEmail;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public String getSenderEmail() {
        return senderEmail;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setTimeSent(String timeSent) {
//        this.timeSent = timeSent;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public boolean isAddressed() {
        return isAddressed;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setAddressed(boolean addressed) {
//        isAddressed = addressed;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/14/18, 6:04 PM):
//    public void address() {
//        isAddressed = true;
//    }
// --Commented out by Inspection STOP (4/14/18, 6:04 PM)

    @Override
    public String toString() {
        return senderEmail + "\n" + timeSent;
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public int compareTo(@NonNull Message other) {
        long thisTime = new Date(this.getTimeSent()).getTime();
        long otherTime = new Date(other.getTimeSent()).getTime();
        return (int)(otherTime - thisTime);
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public boolean equals(Object other) {
        return (other != null) && (other instanceof Message) && ((other == this)
                || (((Message) other).getTimeSent().equals(this.getTimeSent())
                && ((Message) other).getSenderEmail().equalsIgnoreCase(this.getSenderEmail())
                && ((Message) other).getMessage().equalsIgnoreCase(this.getMessage())));
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public int hashCode() {
        int result = 17;
        result = (result * 31) + this.getSenderEmail().toLowerCase().hashCode();
        result = (result * 31) + this.getMessage().toLowerCase().hashCode();
        result = (result * 31) + this.getTimeSent().toLowerCase().hashCode();
        return result;
    }
}
