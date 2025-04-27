package com.example.addressbook.model;


// User class with getters and setters for
public class User {
    private String userName;
    private String password;
    private String email;

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {return userName;}

    public void setUserName(String userName) { this.userName = userName;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}