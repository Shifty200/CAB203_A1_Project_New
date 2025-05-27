package com.example.quizapp.controller;


import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.SQLiteUserDAOLive;
import com.example.quizapp.model.CurrentUser;
import com.example.quizapp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button cancelButton;

    @FXML
    private void onLoginClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (new SQLiteUserDAOLive().checkUserPresent(username) && verifyPassword(password,new SQLiteUserDAOLive().getUser(username).getPassword()))
        {
            System.out.println("Login successful!");
            User currentUser = new SQLiteUserDAOLive().getUser(username);
            CurrentUser.setInstance(currentUser);
            errorLabel.setVisible(false);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/quizapp/dashboard.fxml"));
                Scene dashboardScene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Dashboard");
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Failed to load Dashboard.");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Invalid username or password");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void onCancelClick() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setTitle("TutorWorm");
        stage.setScene(scene);    }

    public boolean verifyPassword(String inputPassword, String storedPassword) {
        // Case 1: stored password looks like a bcrypt hash
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            // Verify with bcrypt
            return BCrypt.checkpw(inputPassword, storedPassword);
        } else {
            // Case 2: stored password is likely plain text
            return inputPassword.equals(storedPassword);
        }
    }
}

