package com.example.quizapp.model;

/**
 * A class representing the currently logged-in user.
 */
public class CurrentUser {
    /**
     * The current user
     */
    private static User instance;

    /**
     * Static method that returns the current user that is logged-in.
     * @return the user object
     */
    public static User getInstance() {
        return instance;
    }

    /**
     * Static method that sets the current user that is logged in.
     * @param user the user to set as logged in.
     */
    public static void setInstance(User user) {
        instance = user;
    }
}
