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


/**
 * A class for interacting with the password changing view of the settings page of the Tutor Worm App
 */
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

    /**
     * Handles returning to the Account view of the settings page when the Account button is pressed.
     */
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

    /**
     * Handles going to the Account view of the settings page when the back button is pressed.
     */
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


    /**
     * Handles changing the user's password in the database to the text in the 'new password' field of the settings password changing view.
     * All 3 passwords must be filled, the old password field must match the current password, and the 'new password' and 'confirm password' field must match.
     * Once the password is successfully changed, it transitions back to the Account view of the settings page.
     * Errors call the QuizAppAlert popup.
     */
    @FXML
    public void handleChangePassword() throws IOException {
        String oldPassword = passwordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        User currentUser = CurrentUser.getInstance();
        String currentPassword = currentUser.getPassword();

        if (Settings.passwordNotFilled(oldPassword, newPassword, confirmPassword)) {
            new QuizAppAlert().alert("Error", "You are missing fields!", "Please ensure all password fields are filled.");
        } else if (!Settings.sameString(newPassword, confirmPassword)) {
            new QuizAppAlert().alert("Error", "Passwords do not match!", "Please confirm your new password.");
        } else if (! new LoginController().verifyPassword(oldPassword, currentPassword)) {
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
