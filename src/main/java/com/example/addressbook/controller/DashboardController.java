package com.example.addressbook.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {

    @FXML
    private VBox addQuizBox;

    @FXML
    public void initialize() {
        addQuizBox.setOnMouseClicked((MouseEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/addressbook/quiz-init/quiz-init.fxml"));
                Scene quizInitScene = new Scene(loader.load(), 900, 600);

                Stage stage = (Stage) addQuizBox.getScene().getWindow();
                stage.setScene(quizInitScene);
                stage.setTitle("Quiz Initialisation");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
