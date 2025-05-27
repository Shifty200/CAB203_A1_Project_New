package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.CurrentUser;
import com.example.quizapp.model.SQLiteUserDAOLive;
import com.example.quizapp.model.QuizAppAlert;
import com.example.quizapp.model.User;
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
import java.util.Objects;

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

    public void initialize() {
        Image img = new Image(getClass().getResource("/com/example/images/user-icon.png").toString());
        userIcon.setFill(new ImagePattern(img));
        setUsername();
        setEmailField();
    }

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

    public void handleChangeEmail() throws IOException {

        User currentUser = CurrentUser.getInstance();
        String currentUserName = currentUser.getUserName();
        String currentPassword = currentUser.getPassword();
        String oldEmail = currentUser.getEmail();

        String newEmail = emailField.getText();

        if (Objects.equals(oldEmail, newEmail)){
            QuizAppAlert sameAlert = new QuizAppAlert();
            sameAlert.alert("Error", "Enter a new email", "This email is the same as the current email registered to this account. Please enter a different email.");
        }
        else if (!newEmail.contains("@")){
            QuizAppAlert emailAlert = new QuizAppAlert();
            emailAlert.alert("Error", "Not Valid", "The email entered is not a valid email");
        }
        else{
            User newUser = new User(currentUserName, currentPassword, newEmail);
            new SQLiteUserDAOLive().updateUser(newUser);
            setEmailField();

            emailField.setPromptText(newEmail);
            QuizAppAlert changedEmail = new QuizAppAlert();
            changedEmail.alert("Email Changed", "Your email was successfully changed!", "");
            Stage stage = (Stage) changeEmailButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        }

    }

    public void setUsername() {
        String currentUsername = CurrentUser.getInstance().getUserName();
        usernameText.setText(currentUsername);
    }

    public void setEmailField(){
        String currentEmail = CurrentUser.getInstance().getEmail();
        emailField.setPromptText(currentEmail);
    }
}
