package com.example.quizapp.model;


/**
 * A class representing the user with their associated credentials.
 */
public class User {
    private String userName;
    private String password;
    private String email;

    /**
     * Constructs a new User with the provided username, password, and email.
     *
     * @param userName the user's username
     * @param password the user's password
     * @param email the user's email address
     */
    public User(String userName, String password, String email) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    /**
     * Returns the user's username.
     * @return the username
    */
    public String getUserName() {return userName;}
    /**
     * Sets the user's username.
     * @param userName the new username
     */
    public void setUserName(String userName) { this.userName = userName;}
    /**
     * Returns the user's password.
     * @return the password
     */
    public String getPassword() {return password;}
    /**
     * Updates the user's password.
     * @param password the new password
     */
    public void setPassword(String password) {this.password = password;}
    /**
     * Returns the user's email.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
    /**
     * Updates the user's email.
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}