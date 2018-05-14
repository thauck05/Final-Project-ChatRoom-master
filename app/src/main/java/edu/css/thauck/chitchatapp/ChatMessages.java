package edu.css.thauck.chitchatapp;

import java.io.Serializable;
import java.util.Date;
/*
 * Model to store chat messages in DB.
 * Layout has three views: messageText, messageAuthor, messageTime
 * Model has these views in order to populate the views in the layout.
 * To make model compatible with FirebaseUI, add default constructor, getters, setters.
 */
public class ChatMessages {
    private String messageText;
    private String messageAuthor;
    private long messageTime;

    /**
     * @param messageText
     * @param messageAuthor
     */
    public ChatMessages(String messageText, String messageAuthor) {
        this.messageText = messageText;
        this.messageAuthor = messageAuthor;
        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessages() {
    }

    public String getMessageText() {
        return messageText;
    }

    /**
     * @param messageText
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * @return
     */
    public String getMessageAuthor() {
        return messageAuthor;
    }

    /**
     * @param messageAuthor
     */
    public void setMessageAuthor(String messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    /**
     * @return
     */
    public long getMessageTime() {
        return messageTime;
    }

    /**
     * @param messageTime
     */
    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
