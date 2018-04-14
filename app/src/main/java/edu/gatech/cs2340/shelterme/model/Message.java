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

    /**
     * A constructor for Message class
     * @param message the message
     * @param sender the sender of the message
     */
    public Message(String message, Account sender) {
        this.message = message;
        this.timeSent = Calendar.getInstance().getTime().toString();
        this.senderEmail = sender.getEmail();
        this.isAddressed = false;
    }

    /**
     * A no-arg constructor for Message class
     */
    public Message() {
        this("No Message Given", new User());
    }


    /**
     * A getter for message
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * A setter for messahe
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * A getter for timeSent
     * @return the time the message was sent
     */
    public String getTimeSent() {
        return timeSent;
    }

    /**
     * A setter for the sender's email
     * @param senderEmail the sender's email
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * A getter for senderEmail
     * @return the sender's email
     */
    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * A setter for the time the message was sent
     * @param timeSent the time the message was sent
     */
    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    /**
     * Checks if the message has an address to be sent to
     * @return true if the message has an address, false otherwise
     */
    public boolean isAddressed() {
        return isAddressed;
    }

    /**
     * A setter for addressed
     * @param addressed the status of the address of the message
     */
    public void setAddressed(boolean addressed) {
        isAddressed = addressed;
    }

    /**
     * Sets the status of the message's address to true
     */
    public void address() {
        isAddressed = true;
    }

    /**
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return senderEmail + "\n" + timeSent;
    }

    /**
     * Compares two objects of type Message
     * @param other an object of type Message
     * @return an integer representing time
     */
    @Override
    public int compareTo(@NonNull Message other) {
        long thisTime = new Date(this.getTimeSent()).getTime();
        long otherTime = new Date(other.getTimeSent()).getTime();
        return (int)(otherTime - thisTime);
    }

    /**
     * Checks if two messages equal eachother based on the time it was sent, the sender's email,
     * and the message itself.
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Message)) return false;
        return (other == this) || (((Message) other).getTimeSent().equals(this.getTimeSent())
                && ((Message) other).getSenderEmail().equalsIgnoreCase(this.getSenderEmail())
                && ((Message) other).getMessage().equalsIgnoreCase(this.getMessage()));
    }

    /**
     * A method that created a hashcode
     * @return the hashcode as an integer
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + this.getSenderEmail().toLowerCase().hashCode();
        result = result * 31 + this.getMessage().toLowerCase().hashCode();
        result = result * 31 + this.getTimeSent().toLowerCase().hashCode();
        return result;
    }
}
