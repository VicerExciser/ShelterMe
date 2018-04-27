package edu.gatech.cs2340.shelterme.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * The type Message.
 */
public abstract class Message implements Comparable<Message> {
    private final String message;
    private final String timeSent;
    private final String senderEmail;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean isAddressed;
    /**
     * The Account unlock requested.
     */
    protected boolean accountUnlockRequested;

    /**
     * Instantiates a new Message.
     *
     * @param message the message
     * @param sender  the sender
     */
    public Message(String message, @NonNull Account sender) {
        this.message = message;
        //noinspection ChainedMethodCall
        Calendar calendar = Calendar.getInstance();
        this.timeSent = calendar.getTime().toString();
        this.senderEmail = sender.getEmail();
        this.isAddressed = false;
    }

    /**
     * Instantiates a new Message.
     */
    public Message() {
        this("No Message Given", new User());
    }


    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setMessage(String message) {
//        this.message = message;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    /**
     * Gets time sent.
     *
     * @return the time sent
     */
    public String getTimeSent() {
        return timeSent;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setSenderEmail(String senderEmail) {
//        this.senderEmail = senderEmail;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    /**
     * Gets sender email.
     *
     * @return the sender email
     */
    public String getSenderEmail() {
        return senderEmail;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setTimeSent(String timeSent) {
//        this.timeSent = timeSent;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    /**
     * Is addressed boolean.
     *
     * @return the boolean
     */
    public boolean isAddressed() {
        return isAddressed;
    }

    /**
     * Mark as addressed.
     */
    public void markAsAddressed() {
        this.setAddressed(true);
        Model.markMessageAsAddressed(this);
    }

    /**
     * Sets addressed.
     *
     * @param addressed the addressed
     */
    public void setAddressed(boolean addressed) {
        this.isAddressed = addressed;
    }

    /**
     * Is account unlock requested boolean.
     *
     * @return the boolean
     */
    public boolean isAccountUnlockRequested() {
        return this.accountUnlockRequested;
    }

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

    /**
     * Resolve intent.
     *
     * @return the intent
     */
    public abstract Intent resolve();
//    {
//        Log.e("MESSAGE", "Superclass 'resolve()' called");
//        return new Intent();
//    }
}
