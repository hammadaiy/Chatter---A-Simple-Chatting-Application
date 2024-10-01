package com.example.chatter;

import java.io.Serializable;

/**
 * This represents a user in the chat.
 * This class is used to store information about the user such as their name and profile photo path.
 * Implements Serializable to allow object serialization for saving and retrieving user data.
 */
public class User implements Serializable {
    // The username of the user
    private String username;

    // The path to the user's profile photo
    private String profilePhotoPath;

    /**
     * Constructs a new User with the specified username.
     *
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username as a String.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the profile photo path of the user.
     *
     * @return The profile photo path as a String.
     */
    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    /**
     * Sets the profile photo path for the user.
     *
     * @param profilePhotoPath The path to the user's profile photo.
     */
    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }
}