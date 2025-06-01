package com.example.quizapp.model;
import java.util.Objects;

/**
 * Class containing static utility methods required for the settings pages.
 */
public class Settings {
    /**
     * Method checks if two strings match.
     * @param firstString the first string
     * @param secondString the second string
     * @return True if the strings are the same
     */
    public static boolean sameString(String firstString, String secondString) {
        return (Objects.equals(firstString, secondString));
    }

    /**
     * Method checks if a string is a 'valid' email
     * @param email the string to check
     * @return True if the string contains the '@' symbol
     */
    public static boolean validEmail(String email) {
        return email.contains("@");
    }

    /**
     * Method updates a user's email in the database
     * @param user the user object to change the email of
     * @param newEmail the updated email to save in the database
     */
    public static void updateUserEmail(User user, String newEmail) {
        User updatedUser = new User(user.getUserName(), user.getPassword(), newEmail);
        new SQLiteUserDAOLive().updateUser(updatedUser);
    }

    /**
     * Method checks if any fields are not filled
     * @param oldPassword the old password field
     * @param newPassword the new password field
     * @param confirmPassword the confirm password field
     * @return True if any of the password changing fields are empty
     */
    public static boolean passwordNotFilled(String oldPassword, String newPassword, String confirmPassword) {
        return (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty());
    }


    /**
     * Method updates a user's password in the database
     * @param user the user object to change the password of
     * @param newPassword the updated password to save in the database
     */
    public static void updateUserPassword(User user, String newPassword) {
        User updatedUser = new User(user.getUserName(), newPassword, user.getEmail());
        new SQLiteUserDAOLive().updateUser(updatedUser);
    }
}




