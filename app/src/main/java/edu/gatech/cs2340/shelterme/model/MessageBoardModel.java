package edu.gatech.cs2340.shelterme.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the message board containing the messages sent and received via the app.
 */
public class MessageBoardModel {
    public static List<Message> messages = new LinkedList<>();
    public static List<Message> unaddressedMessages = new LinkedList<>();

    /**
     * Removes messages that don't have an address
     * @param message the message of the email
     */
    public static void addressMessage(Message message) {
        message.address();
        unaddressedMessages.remove(message);
    }

    /**
     * Adds messages to the messages board
     * @param message the message of the email
     */
    private static void addMessageToMessageBoard(Message message) {
        messages.add(0, message); //adds newest messages to top of list
        unaddressedMessages.add(0, message);
    }

    /**
     * Creates a new message Object amd adds it to message board
     * @param message the message of the email
     * @param sender the sender of the email
     */
    public static void sendNewMessage(String message, User sender) {
        Message newMessage = new Message(message, sender);
        addMessageToMessageBoard(newMessage);
    }

    /**
     * Creates a new user report and adds it to message board
     * @param reportedUser the user being reported
     * @param message the message of the email
     * @param sender the sender of the email
     */
    public static void sendNewReportUserRequest(User reportedUser, String message, Employee sender) {
        ReportUserRequest reportUserRequest = new ReportUserRequest(reportedUser, message, sender);
        addMessageToMessageBoard(reportUserRequest);
    }

    /**
     * Creates a new request to unlock a user
     * @param lockedUser the user account that is locked
     * @param message the message of the email
     * @param sender the sender of the email
     */
    public static void sendNewUnlockRequest(User lockedUser, String message, Account sender) {
        UnlockRequest unlockRequest = new UnlockRequest(lockedUser, message, sender);
        addMessageToMessageBoard(unlockRequest);
    }
}
