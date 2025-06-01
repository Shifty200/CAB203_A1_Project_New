package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * A class for interacting with the main view (Account) of the settings page of the Tutor Worm App
 */

public class SettingsController {

    @FXML
    private Button settingsBack;
    @FXML
    private Button accountButton;

    @FXML
    private Label usernameText;
    @FXML
    private TextField emailField;
    @FXML
    private Button changeEmailButton;
    @FXML
    private Button toPasswordButton;
    @FXML
    private Circle userIcon;

    /**
     * Initialises the Account page by setting the profile picture, username field and the current email.
     */
    public void initialize() {
        Image img = new Image(getClass().getResource("/com/example/images/user-icon.png").toString());
        userIcon.setFill(new ImagePattern(img));
        setUsername();
        setEmailField();
    }

    /**
     * Handles returning to the Dashboard view from the settings page.
     */
    public void settingsBackPressed(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/quizapp/dashboard.fxml"));
            Scene dashboardScene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage stage = (Stage) settingsBack.getScene().getWindow();
            stage.setTitle("Dashboard");
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles going to the Account view of the settings page.
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
     * Handles transitioning to the password changing view.
     */
    @FXML
    private void handleToPasswordScreen() {
        try {
            Stage stage = (Stage) toPasswordButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsPassword-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Handles changing the user's email in the database to the text in the email field of the settings Account view.
     * The email must both be different to the user's current email and contain an '@' symbol.
     */
    public void handleChangeEmail() throws IOException {

        User currentUser = CurrentUser.getInstance();
        String oldEmail = currentUser.getEmail();

        String newEmail = emailField.getText();

        if (Settings.sameString(oldEmail, newEmail)){
            QuizAppAlert sameAlert = new QuizAppAlert();
            sameAlert.alert("Error", "Enter a new email", "This email is the same as the current email registered to this account. Please enter a different email.");
        }
        else if (!Settings.validEmail(newEmail)){
            QuizAppAlert emailAlert = new QuizAppAlert();
            emailAlert.alert("Error", "Not Valid", "The email entered is not a valid email");
        }
        else{
            Settings.updateUserEmail(currentUser, newEmail);
            emailField.setPromptText(newEmail);
            new QuizAppAlert().alert("Email Changed", "Your email was successfully changed!", "You may need to log back in for changes to be displayed inside the app.");

            Stage stage = (Stage) changeEmailButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        }

    }

    /**
     * Sets the settings page username text to the current user's username.
     */
    public void setUsername() {
        String currentUsername = CurrentUser.getInstance().getUserName();
        usernameText.setText(currentUsername);
    }

    /**
     * Sets the settings page 'change email' prompt text to the current user's email.
     */
    public void setEmailField(){
        String currentEmail = CurrentUser.getInstance().getEmail();
        emailField.setPromptText(currentEmail);
    }
}
