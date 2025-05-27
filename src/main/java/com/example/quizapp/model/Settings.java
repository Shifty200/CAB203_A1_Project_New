package com.example.quizapp.model;

import java.util.Objects;

public class Settings {

    public static boolean sameEmail(String oldEmail, String newEmail) {
        return (Objects.equals(oldEmail, newEmail));
    }

    public static boolean validEmail(String email) {
        return email.contains("@");
    }

    public static void updateUserEmail(User user, String newEmail) {
        User updatedUser = new User(user.getUserName(), user.getPassword(), newEmail);
        new SQLiteUserDAOLive().updateUser(updatedUser);
    }


    public static boolean passwordNotFilled(String oldPassword, String newPassword, String confirmPassword) {
        return (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty());
    }

    public static boolean passwordsMatch(String newPassword, String confirmPass) {
        return newPassword.equals(confirmPass);
    }

    public static boolean oldPasswordCorrect(String oldPassword, String currentPassword) {
        return Objects.equals(oldPassword, currentPassword);
    }

    public static void updateUserPassword(User user, String newPassword) {
        User updatedUser = new User(user.getUserName(), newPassword, user.getEmail());
        new SQLiteUserDAOLive().updateUser(updatedUser);
    }
}




