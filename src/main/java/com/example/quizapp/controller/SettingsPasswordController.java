package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.CurrentUser;
import com.example.quizapp.model.SQLiteUserDAOLive;
import com.example.quizapp.model.quizAppAlert;
import com.example.quizapp.model.User;
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

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            quizAppAlert emptyAlert = new quizAppAlert();
            emptyAlert.alert("Error", "You are missing fields!", "Please ensure all password fields are filled.");
        } else if (!newPassword.equals(confirmPassword)) {
            quizAppAlert matchingAlert = new quizAppAlert();
            matchingAlert.alert("Error", "Passwords do not match!", "Please confirm your new password.");
        } else if (!Objects.equals(oldPassword, currentPassword)) {
            quizAppAlert incorrectAlert = new quizAppAlert();
            incorrectAlert.alert("Error", "Old password is incorrect!", "Please ensure your old password is correct.");
        } else {
            User newUser = new User(currentUserName, newPassword, currentEmail);
            new SQLiteUserDAOLive().updateUser(newUser);
            Stage stage = (Stage) changePasswordButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

            quizAppAlert changedPass = new quizAppAlert();
            changedPass.alert("Password Changed", "Your password was successfully changed!", "");
        }
    }

}
