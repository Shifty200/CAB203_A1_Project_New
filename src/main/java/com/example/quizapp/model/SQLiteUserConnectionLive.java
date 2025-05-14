package com.example.quizapp.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteUserConnectionLive {
    private static Connection instance = null;

    private SQLiteUserConnectionLive() {
        String url = "jdbc:sqlite:users.db";

        try {
            instance = DriverManager.getConnection(url);
            instance.createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new SQLiteUserConnectionLive();
        }
        return instance;
    }
}
