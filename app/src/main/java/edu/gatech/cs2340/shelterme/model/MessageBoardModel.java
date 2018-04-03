package edu.gatech.cs2340.shelterme.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MessageBoardModel {
    public static List<Message> messages = new ArrayList<>();
    public static List<Message> unaddressedMessages = new LinkedList<>();

    public static void addressMessage(Message message) {
        message.address();
        unaddressedMessages.remove(message);
    }

    private static void addMessageToMessageBoard(Message message) {
        messages.add(message);
        unaddressedMessages.add(message);
    }

    public static void sendNewMessage(String message, User sender) {
        Message newMessage = new Message(message, sender);
        addMessageToMessageBoard(newMessage);
    }

    public static void sendNewReportUserRequest(User reportedUser, String message, Employee sender) {
        ReportUserRequest reportUserRequest = new ReportUserRequest(reportedUser, message, sender);
        addMessageToMessageBoard(reportUserRequest);
    }

    public static void sendNewUnlockRequest(User lockedUser, String message, Account sender) {
        UnlockRequest unlockRequest = new UnlockRequest(lockedUser, message, sender);
        addMessageToMessageBoard(unlockRequest);
    }
}
