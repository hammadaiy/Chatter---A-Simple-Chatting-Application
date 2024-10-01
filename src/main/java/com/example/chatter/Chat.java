package com.example.chatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents a chat session in the chat application.
 * This class stores a list of messages as part of the chat history.
 * Implements Serializable to allow object serialization for saving and retrieving chat data.
 */
public class Chat implements Serializable {
    // List to store the messages of the chat
    private List<Message> messages;

    /**
     * Constructs a new Chat with an empty list of messages.
     */
    public Chat() {
        this.messages = new ArrayList<>();
    }

    /**
     * Adds a message to the chat.
     *
     * @param message The message to be added to the chat.
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Retrieves the list of messages in the chat.
     *
     * @return A list of messages.
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages for the chat.
     *
     * @param messages The list of messages to set.
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}