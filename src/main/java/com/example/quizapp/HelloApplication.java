package com.example.quizapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The main class for the Tutor Worm app.
 * This class contains constants required and handles the applications initialisation.
 */
public class HelloApplication extends Application {
    // Constants defining the window title and size
    public static final String TITLE = "Tutor Worm";
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    /**
     * Starts the application and shows the landing page view.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        Image image = new Image(getClass().getResource("/com/example/images/tutorworm-default.png").toString());
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.setTitle("TutorWorm");
        stage.show();
    }

    /**
     * Tutor Worm program entry point to start the JavaFX app.
     */
    public static void main(String[] args) {
        launch();
    }
}