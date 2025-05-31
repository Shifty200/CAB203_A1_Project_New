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
import javafx.scene.control.ComboBox;
import com.example.quizapp.model.SQLiteQuizDAOLive;


import java.io.File;
import java.util.List;

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
    @FXML private TextField quizTitle;
    @FXML private ComboBox<String> topicDropdown;



    private File selectedFile;
    private String questionRange;
    private String uploadedFileContent;
    private final SQLiteQuizDAOLive quizDAO = new SQLiteQuizDAOLive();

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

        List<String> topicsFromDB = quizDAO.getAllTopicsByCurrentUser();
        topicDropdown.getItems().addAll(topicsFromDB);
        topicDropdown.setPromptText("Select Topic");

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

            String topic = topicDropdown.getValue();
            if (topic == null || topic.trim().isEmpty()) {
                errorLabel.setText("Please select a topic.");
                errorLabel.setVisible(true);
                return;
            }

            String enteredTitle = quizTitle.getText();
            if (enteredTitle == null || enteredTitle.trim().isEmpty()) {
                errorLabel.setText("Please enter a quiz title.");
                errorLabel.setVisible(true);
                return;
            }

            String difficulty = getDifficultyLabel(difficultySlider.getValue());

            String prompt = "Create a quiz with " + questionRange + " questions on " + topic + "named " + enteredTitle +
                    " for high school students with " + difficulty + " difficulty." +
                    "\nFollow all of these instructions:" +
                    "\n1. Ensure that no questions are repeated, and that no two questions are too similar to each other." +
                    "\n2. Each question must have exactly one correct answer. Provide a correctIndex indicating the index of this answer " +
                    "in the options array." +
                    "\n3. Indexing for correctIndex starts from 0. Do not provide a correctIndex that is out of range. " +
                    "E.g. if the question has 4 options, the correctIndex can only be 0, 1, 2, or 3." +
                    "\n4. Do not provide empty or meaningless options. " +
                    "E.g. for a yes or no question, the options should be ['Yes', 'No'], not ['.', 'Yes', 'No', '.']" +
                    "\n5. Do not generate more questions than requested. " +
                    "E.g. if you have been asked for 10-20 questions, do not generate more than 20 questions." +
                    "\n6. Questions must be answerable using only the information in the provided study material." +
                    "\n7. Do not use the character ',' in options. If necessary, use ':', ';', '.', or '--' instead." +
                    "\n\nUse the following study material to create the quiz:\n\n" + uploadedFileContent;

            Stage loadingStage = QuizAppAlert.loadingSpinner("Generating Quiz...", startQuizBtn);
            loadingStage.show();

            new Thread(() -> {
                Quiz quiz = new Quiz();
                boolean success = true;
                do {
                    try {
                        String jsonResponse = AIQuizGenerator.generateQuiz(prompt);
                        System.out.println("Raw AI Response:\n" + jsonResponse);
                        quiz = QuizAppUtil.parseAIResponse(jsonResponse, enteredTitle, topic, difficulty);
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
        return true;
    }

    public static String getDifficultyLabel(double sliderValue) {
        if (sliderValue < 1.5) return "easy";
        if (sliderValue < 2.5) return "medium";
        return "hard";
    }
}