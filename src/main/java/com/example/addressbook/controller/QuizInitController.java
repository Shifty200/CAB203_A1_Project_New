package com.example.addressbook.controller;

import com.example.addressbook.model.AppState;
import com.example.addressbook.model.QuizInitConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class QuizInitController {

    @FXML private VBox uploadBox;
    @FXML private Label errorLabel;
    @FXML private Slider difficultySlider;
    @FXML private ToggleButton q1to10;
    @FXML private ToggleButton q10to20;
    @FXML private ToggleButton q20to30;
    @FXML private Button startQuizBtn;
    @FXML private Button backToDashboardBtn;


    private File selectedFile;
    private String questionRange;

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        q1to10.setToggleGroup(group);
        q10to20.setToggleGroup(group);
        q20to30.setToggleGroup(group);
        errorLabel.setVisible(false);
        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                questionRange = ((ToggleButton) newToggle).getText();
            }
        });
        uploadBox.setOnMouseClicked((MouseEvent e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Study Material File");
            selectedFile = fileChooser.showOpenDialog(uploadBox.getScene().getWindow());

            if (selectedFile != null) {
                errorLabel.setText("Selected: " + selectedFile.getName());
                errorLabel.setVisible(true);
            } else {
                errorLabel.setText("No file selected.");
                errorLabel.setVisible(true);
            }
        });
        startQuizBtn.setOnAction(e -> {
            if (selectedFile == null) {
                errorLabel.setText("Please upload a file first!");
                return;
            }

            if (questionRange == null || questionRange.isEmpty()) {
                errorLabel.setText("Please select question range.");
                return;
            }

            storeQuizInit();
        });
        backToDashboardBtn.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/addressbook/Dashboard/Dashboard.fxml"));
                Scene dashboardScene = new Scene(loader.load(), 900, 600);
                Stage stage = (Stage) backToDashboardBtn.getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Dashboard");
            } catch (Exception ex) {
                ex.printStackTrace();
                errorLabel.setText("Failed to return to dashboard.");
            }
        });
    }

    private void storeQuizInit() {
        double difficulty = difficultySlider.getValue();

        QuizInitConfig config = new QuizInitConfig(selectedFile, difficulty, questionRange);
        AppState.currentQuizConfig = config;

        System.out.println("Quiz config stored:");
        System.out.println("File: " + config.getUploadedFile().getName());
        System.out.println("Difficulty: " + config.getDifficulty());
        System.out.println("Questions: " + config.getQuestionRange());
    }


}
