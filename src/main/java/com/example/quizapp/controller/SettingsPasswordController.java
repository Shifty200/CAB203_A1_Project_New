package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SettingsPasswordController {
    @FXML
    private Button accountButton;
    @FXML
    private Button passwordBack;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button changePasswordButton;

    @FXML
    private void handleAccount() {
        try {
            Stage stage = (Stage) accountButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //settingsPassword-view.fxml
    @FXML
    public void passwordBackPressed() {
        try {
            Stage stage = (Stage) passwordBack.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleChangePassword() throws IOException {
        String oldPassword = passwordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        User currentUser = CurrentUser.getInstance();
        String currentUserName = currentUser.getUserName();
        String currentEmail = currentUser.getEmail();
        String currentPassword = currentUser.getPassword();

        if (Settings.passwordNotFilled(oldPassword, newPassword, confirmPassword)) {
            new QuizAppAlert().alert("Error", "You are missing fields!", "Please ensure all password fields are filled.");
        } else if (!Settings.passwordsMatch(newPassword, confirmPassword)) {
             new QuizAppAlert().alert("Error", "Passwords do not match!", "Please confirm your new password.");
        } else if (!Settings.oldPasswordCorrect(oldPassword, currentPassword)) {
            new QuizAppAlert().alert("Error", "Old password is incorrect!", "Please ensure your old password is correct.");
        } else {
            Settings.updateUserPassword(currentUser, newPassword);

            Stage stage = (Stage) changePasswordButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

            new QuizAppAlert().alert("Password Changed", "Your password was successfully changed!", "");
        }
    }

}
