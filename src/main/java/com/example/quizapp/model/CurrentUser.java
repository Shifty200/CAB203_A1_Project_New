package com.example.quizapp.model;

public class CurrentUser {
    private static User instance;

    private CurrentUser() {}

    public static User getInstance() {
        return instance;
    }

    public static void setInstance(User user) {
        instance = user;
    }
}
