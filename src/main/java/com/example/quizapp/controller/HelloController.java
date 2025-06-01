package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


import java.io.IOException;

/**
 * A class handling FXML for the initial landing page of the Tutor Worm app.
 */
public class HelloController {

    @FXML
    private ImageView tutorWorm;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;

    /**
     * Initialises the landing view by setting the tutorWorm image.
     */
    @FXML
    public void initialize() {
        System.out.println("Looking for image at: " + getClass().getResource("/com/example/images/tutorworm-default.png"));
        Image image = new Image(getClass().getResource("/com/example/images/tutorworm-default.png").toString());
        tutorWorm.setImage(image);

    }

    /**
     * Transitions to the login view
     * @throws IOException
     */
    @FXML
    protected void onLoginClick() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setTitle("Login");
        stage.setScene(scene);
    }

    /**
     * Transitions to the signUp view
     * @throws IOException
     */
    @FXML
    protected void onSignUpClick() throws IOException {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setTitle("Sign up");
        stage.setScene(scene);
    }
}