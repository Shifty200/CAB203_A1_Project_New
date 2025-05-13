package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.SQLiteQuizDAOLive;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML private Button settingsButton;
    @FXML private VBox addQuizInit;
    @FXML private ComboBox<String> topicDropdown;
    @FXML private Button viewProgressBtn;
    @FXML private Hyperlink logoutLink;
    @FXML private Circle userIcon;

    @FXML
    public void initialize() {

        Image img = new Image(getClass().getResource("/com/example/images/user-icon.png").toString());
        userIcon.setFill(new ImagePattern(img));

        // list all of the topics that were added in the quiz init -- otherwise hide them
        List<String> topics = new SQLiteQuizDAOLive().getAllTopics();
        if (topics.isEmpty()) {
            topicDropdown.setVisible(false);
            viewProgressBtn.setVisible(false);
        } else {
            topicDropdown.getItems().addAll(topics);
            topicDropdown.setValue(topics.get(0));
        }

        // start the quiz init
        addQuizInit.setOnMouseClicked((MouseEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz-init.fxml"));
                Scene quizInitScene = new Scene(loader.load(), 900, 600);
                Stage stage = (Stage) addQuizInit.getScene().getWindow();
                stage.setScene(quizInitScene);
                stage.setTitle("Quiz Initialisation");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        viewProgressBtn.setOnAction(event -> {
            String selectedTopic = topicDropdown.getValue();
            if (selectedTopic != null) {
                System.out.println("Viewing progress for topic: " + selectedTopic);
                // progress page to be implemented here
            }
        });

        logoutLink.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/login-view.fxml"));
                Scene loginScene = new Scene(loader.load(), 900, 600);
                Stage stage = (Stage) logoutLink.getScene().getWindow();
                stage.setScene(loginScene);
                stage.setTitle("Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void settingsPressed() throws IOException {
        try {
            Stage stage = (Stage) settingsButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settingsProfile-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setTitle("Settings");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
