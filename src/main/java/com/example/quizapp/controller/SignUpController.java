package com.example.quizapp.controller;


import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.SQLiteUserDAOLive;
import com.example.quizapp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;
    @FXML private Button cancelButton;
    @FXML private Button signupButton;

    @FXML
    private void onSignUpClick() throws IOException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("All fields are required.");
        } else if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match.");
        } else if (!email.contains("@")) {
            showMessage("Invalid email format.");
        } else if (new SQLiteUserDAOLive().checkUserPresent(username)){
            showMessage("This username already exists.");
        } else {
            new SQLiteUserDAOLive().addUser(new User(username,password, email));
            System.out.println("Sign-up successful for user: " + username);
            showMessage("Sign-up successful!", false);
            Stage stage = (Stage) signupButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setTitle("TutorWorm");
            stage.setScene(scene);

        }
    }

    @FXML
    private void onCancelClick() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setTitle("TutorWorm");
        stage.setScene(scene);    }

    private void showMessage(String message) {
        showMessage(message, true);
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.GREEN);
        messageLabel.setVisible(true);
    }
}