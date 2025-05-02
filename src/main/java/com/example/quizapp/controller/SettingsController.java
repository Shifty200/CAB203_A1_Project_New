package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.CurrentUser;
import com.example.quizapp.model.SQLiteUserDAOLive;
import com.example.quizapp.model.quizAppAlert;
import com.example.quizapp.model.User;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;


public class SettingsController {

    @FXML
    private Button settingsBack;
    @FXML
    private Button passwordBack;
    @FXML
    private Label messageBox;
    @FXML
    private Button accountButton;
    @FXML
    private Button personalisationButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private Button logoutButton;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button toPasswordButton;

    @FXML
    private Button changePasswordButton;


    public void settingsBackPressed(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/quizapp/Dashboard/Dashboard.fxml"));
            Scene dashboardScene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage stage = (Stage) settingsBack.getScene().getWindow();
            stage.setTitle("Dashboard");
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle(HelloApplication.TITLE);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    @FXML
    private void handlePersonalisation() {
        setMessageBox("Personalisation tab clicked", 2);
        //System.out.println(currentUser.getUserName());
        //System.out.println(currentUser.getEmail());
    }

    @FXML
    private void handleNotifications() {
        setMessageBox("Notifications tab clicked", 2);
    }


    public void handleChangeUsername(ActionEvent actionEvent) {
        setMessageBox("Change username clicked", 2);
    }

    public void handleChangeEmail(ActionEvent actionEvent) {
        setMessageBox("Change email clicked", 2);
    }

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
            new User(currentUserName, newPassword, currentEmail);
            new SQLiteUserDAOLive().updateUser(currentUser);
            Stage stage = (Stage) changePasswordButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        }
    }


    public void setMessageBox(String message, Integer time) {
        messageBox.setText(message);
        PauseTransition waiting = new PauseTransition(Duration.seconds(time));
        waiting.setOnFinished(event -> messageBox.setText(""));
        waiting.play();
    }
}
