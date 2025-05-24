package com.example.quizapp.model;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.IOException;



public class QuizAppAlert {

        public Alert createAlert(String title, String header, String content) throws IOException {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            Image image = new Image(getClass().getResource("/com/example/images/tutorworm-incorrect.png").toString());
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            imageView.setImage(image);
            alert.setGraphic(imageView);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);

            return alert;
        }

        public void alert(String title, String header, String content) throws IOException {
        Alert alert = createAlert(title, header, content);
        Stage alert_window = (Stage) alert.getDialogPane().getScene().getWindow();
        alert_window.getIcons().add(new Image(getClass().getResource("/com/example/images/tutorworm-default.png").toString()));
        alert.showAndWait();
    }
}