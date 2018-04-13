package edu.gatech.cs2340.shelterme.model;

import java.util.LinkedList;
import java.util.List;

class MessageBoardModel {
    private static final List<Message> messages = new LinkedList<>();
    private static final List<Message> unaddressedMessages = new LinkedList<>();

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public static void addressMessage(Message message) {
//        message.address();
//        unaddressedMessages.remove(message);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    private static void addMessageToMessageBoard(Message message) {
        messages.add(0, message); //adds newest messages to top of list
        unaddressedMessages.add(0, message);
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public static void sendNewMessage(String message, User sender) {
//        Message newMessage = new Message(message, sender);
//        addMessageToMessageBoard(newMessage);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public static void sendNewReportUserRequest(User reportedUser, String message, Employee sender) {
//        ReportUserRequest reportUserRequest = new ReportUserRequest(reportedUser, message, sender);
//        addMessageToMessageBoard(reportUserRequest);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public static void sendNewUnlockRequest(User lockedUser, String message, Account sender) {
//        UnlockRequest unlockRequest = new UnlockRequest(lockedUser, message, sender);
//        addMessageToMessageBoard(unlockRequest);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
}
