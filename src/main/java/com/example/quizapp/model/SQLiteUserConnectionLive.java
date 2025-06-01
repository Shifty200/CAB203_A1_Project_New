package com.example.quizapp.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton management of the Tutor Worm database.
 */
public class SQLiteUserConnectionLive {
    private static Connection instance = null;

    /**
     * Establishes a connection with the database.
     */
    private SQLiteUserConnectionLive() {
        String url = "jdbc:sqlite:users.db";

        try {
            instance = DriverManager.getConnection(url);
            instance.createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    /**
     * Gets the instance of the DB connection
     * @return the connection instance.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SQLiteUserConnectionLive();
        }
        return instance;
    }
}
