package com.example.quizapp.model;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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

    public static Stage loadingSpinner(String labelText, Button source) {
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(100, 100);

        Label label = new Label(labelText);
        VBox vbox = new VBox(spinner, label);
        vbox.setSpacing(20);
        vbox.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        Scene scene = new Scene(vbox);
        Stage dialog = new Stage();
        dialog.setScene(scene);
        dialog.setTitle("Please Wait");
        dialog.setWidth(250);
        dialog.setHeight(200);
        dialog.setResizable(false);
        dialog.initOwner(source.getScene().getWindow());
        dialog.setAlwaysOnTop(true);
        Image image = new Image(QuizAppAlert.class.getResource("/com/example/images/tutorworm-default.png").toString());
        dialog.getIcons().add(image);

        return dialog;
    }
}