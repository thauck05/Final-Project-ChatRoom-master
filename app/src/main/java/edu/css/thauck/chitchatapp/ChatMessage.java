package edu.css.thauck.chitchatapp;

import java.util.Date;
/*
 * Model to store chat messages in DB.
 * Layout has three views: messageText, messageAuthor, messageTime
 * Model has these views in order to populate the views in the layout.
 * To make model compatible with FirebaseUI, add default constructor, getters, setters.
 */
public class ChatMessage {
    private String messageText;
    private String messageAuthor;
    private long messageTime;

    public ChatMessage(String messageText, String messageAuthor) {
        this.messageText = messageText;
        this.messageAuthor = messageAuthor;
        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageAuthor() {
        return messageAuthor;
    }

    public void setMessageAuthor(String messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
