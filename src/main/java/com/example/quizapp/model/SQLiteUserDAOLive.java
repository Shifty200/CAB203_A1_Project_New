package com.example.quizapp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;



public class SQLiteUserDAOLive implements IUserDAO {
    private Connection connection;

    public SQLiteUserDAOLive() {
        connection = SQLiteUserConnectionLive.getInstance();
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "userName VARCHAR PRIMARY KEY NOT NULL,"
                    + "password VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (userName, password, email) VALUES (?, ?, ?)");
            statement.setString(1, user.getUserName());
            String hashPW = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            statement.setString(2, hashPW);
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
            // Set the id of the new contact
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET email = ?, password = ? WHERE userName = ?");
            statement.setString(1, user.getEmail());
            String hashPW = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            statement.setString(2, hashPW);
            statement.setString(3, user.getUserName());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE userName = ?");
            statement.setString(1, user.getUserName());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE userName = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                User user = new User(userName, password, email);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                User user = new User(userName, password, email);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(users);
    }

    @Override
    public boolean checkUserPresent(String userName){
        List<User> users = getAllUsers();
        int userLength = users.size();
        for (int i=0 ; i < userLength; i++){
            if(Objects.equals(users.get(i).getUserName(), userName)){
                return true;
            }
        }
        return false;
        
    }
}

