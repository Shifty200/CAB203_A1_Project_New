package com.example.addressbook.controller;


import com.example.addressbook.HelloApplication;
import com.example.addressbook.model.SQLiteConnectionLive;
import com.example.addressbook.model.SQLiteUserDAOLive;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

        if (new SQLiteUserDAOLive().checkUserPresent(username) && new SQLiteUserDAOLive().getUser(username).getPassword().equals(password))
        {
            System.out.println("Login successful!");
            errorLabel.setVisible(false);
            // Navigate to another view or perform action
            // TODO
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
        stage.setScene(scene);    }
}

