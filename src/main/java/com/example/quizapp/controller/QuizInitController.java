package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static com.example.quizapp.model.QuizInitConfig.readLinesFromFile;

public class QuizInitController {

    @FXML private VBox uploadBox;
    @FXML private Label errorLabel;
    @FXML private Slider difficultySlider;
    @FXML private ToggleButton q1to10;
    @FXML private ToggleButton q10to20;
    @FXML private ToggleButton q20to30;
    @FXML private Button startQuizBtn;
    @FXML private Button backToDashboardBtn;
    @FXML private TextField topicField;


    private File selectedFile;
    private String questionRange;
    private String uploadedFileContent;

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
                try {
                    uploadedFileContent = readLinesFromFile(selectedFile.getAbsolutePath());
                } catch (Exception error) {
                    error.printStackTrace();
                    errorLabel.setText("Error reading file.");
                }
            } else {
                errorLabel.setText("No file selected.");
                errorLabel.setVisible(true);
            }
        });

        startQuizBtn.setOnAction(e -> {
            if (!validateInputs()) return;

            String topic = topicField.getText().trim();
            String difficulty = getDifficultyLabel(difficultySlider.getValue());

            String prompt = "Create a quiz with " + questionRange + " questions on " + topic +
                    " for high school students with " + difficulty + " difficulty using the following study material:\n\n" +
                    uploadedFileContent + "\n\n" +
                    "Pay attention to the following rules:" +
                    "\n1. Ensure that no questions are repeated, and that no two questions are too similar to each other." +
                    "\n2. Indexing for correctIndex starts from 0. Do not provide a correctIndex that is out of range. " +
                    "E.g. if the question has 4 options, the correctIndex cannot be 4." +
                    "\n3. Do not provide empty or meaningless options. " +
                    "E.g. for a yes or no question, the options should be ['Yes', 'No'], not ['.', 'Yes', 'No', '.']" +
                    "\n4. Do not generate more questions than requested. " +
                    "E.g. if you have been asked for 10-20 questions, do not generate more than 20 questions." +
                    "\n5. All questions must be answerable using only the provided study material. Do not generate questions that " +
                    "are related to the topic but cannot be answered using only the provided study material." +
                    "\n6. The number of options for each question must be between 2 and 6 (inclusive). " +
                    "Questions with 4 options should be the most common.";

            Stage loadingStage = loadingSpinner();
            loadingStage.show();

            new Thread(() -> {
                Quiz quiz = new Quiz();
                boolean success = true;
                do {
                    try {
                        String jsonResponse = aiQuizGenerator.generateQuiz(prompt);
                        System.out.println("Raw AI Response:\n" + jsonResponse);
                        String generatedTitle = aiQuizGenerator.generateQuizTitle(jsonResponse);
                        quiz = QuizTakingUtil.parseAIResponse(jsonResponse, generatedTitle, topic, difficulty);
                    } catch (IndexOutOfBoundsException ex) {
                        success = false;
                        System.out.println("The AI provided an out-of-bounds index. Trying again...");
                    } catch (Exception ex) {
                        success = false;
                        System.out.println("An error occurred.");
                        ex.printStackTrace();
                        System.out.println("Trying again...");
                    }
                } while (!success);

                new SQLiteQuizDAOLive().addQuiz(quiz);

                Quiz finalQuiz = quiz;
                Platform.runLater(() -> {
                    loadingStage.close();
                    if (finalQuiz.getLength() == 0) {
                        errorLabel.setText("AI did not return any questions.");
                        return;
                    }
                    try {
                        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("questions-view.fxml"));
                        Scene scene = new Scene(loader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);

                        QuestionsController controller = loader.getController();
                        controller.setQuiz(finalQuiz);

                        Stage stage = (Stage) startQuizBtn.getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Quiz");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        errorLabel.setText("Failed to load quiz screen.");
                    }
                });
            }).start();
        });

        backToDashboardBtn.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/dashboard.fxml"));
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

    private boolean validateInputs() {
        if (selectedFile == null) {
            errorLabel.setText("Please upload a file first!");
            errorLabel.setVisible(true);
            return false;
        }

        if (questionRange == null || questionRange.isEmpty()) {
            errorLabel.setText("Please select question range.");
            errorLabel.setVisible(true);
            return false;
        }

        if (topicField.getText() == null || topicField.getText().trim().isEmpty()) {
            errorLabel.setText("Please enter a topic.");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    public static String getDifficultyLabel(double sliderValue) {
        if (sliderValue < 1.5) return "easy";
        if (sliderValue < 2.5) return "medium";
        return "hard";
    }

    private Stage loadingSpinner() {
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(100, 100);

        Label label = new Label("Generating Quiz...");
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
        dialog.initOwner(startQuizBtn.getScene().getWindow());
        dialog.setAlwaysOnTop(true);
        Image image = new Image(getClass().getResource("/com/example/images/tutorworm-default.png").toString());
        dialog.getIcons().add(image);


        return dialog;
    }

}