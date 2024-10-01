package com.example.chatter;

import java.io.Serializable;
import java.util.Date;

/**
 * This represents a message in the chat.
 * This class stores details about the message including the sender, receiver, content, and timestamp.
 * Implements Serializable to allow object serialization for saving and retrieving message data.
 */
public class Message implements Serializable {
    // The user who sent the message
    private User sender;

    // The intended receiver of the message
    private User receiver;

    // The textual content of the message
    private String content;

    // The timestamp indicating when the message was created
    private Date timestamp;

    /**
     * Constructs a new Message with the specified sender, receiver, and content.
     * The timestamp is automatically set to the current date and time.
     *
     * @param sender   The user sending the message.
     * @param receiver The user who is the intended recipient of the message.
     * @param content  The textual content of the message.
     */
    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date(); // Sets the timestamp to the current date and time
    }

    /**
     * Retrieves the sender of the message.
     *
     * @return The user who sent the message.
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the sender of the message.
     *
     * @param sender The user sending the message.
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Retrieves the receiver of the message.
     *
     * @return The user who is the intended recipient of the message.
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver of the message.
     *
     * @param receiver The user who is the intended recipient of the message.
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * Retrieves the content of the message.
     *
     * @return The textual content of the message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     *
     * @param content The textual content of the message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retrieves the timestamp of the message.
     *
     * @return The date and time when the message was created.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the message.
     *
     * @param timestamp The date and time when the message was created.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}