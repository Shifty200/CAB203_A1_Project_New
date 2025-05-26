package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.quizapp.model.AIFeedbackGenerator.generateFeedback;
import static javafx.scene.Cursor.HAND;

public class DashboardController {

    @FXML private Button settingsButton;
    @FXML private Label userNameLabel;
    @FXML private VBox addQuizInit;
    @FXML private ComboBox<String> topicDropdown;
    @FXML private Button viewProgressBtn;
    @FXML private Hyperlink logoutLink;
    @FXML private Circle userIcon;
    @FXML private HBox quizHistoryBox;

    private String topic = "All Topics";

    @FXML
    private void handleComboBoxSelection() {
        this.topic = topicDropdown.getValue();
        refreshQuizzesDisplay();
    }

    private void refreshQuizzesDisplay() {
        // Clear existing quizzes from the display
        quizHistoryBox.getChildren().clear();

        List<Quiz> quizzes;

        // Display all quizzes if topic not chosen, else, sort by topic
        if (topic == "All Topics") {
            quizzes = new SQLiteQuizDAOLive().getAllQuizzes();
        }
        else {
            quizzes = new SQLiteQuizDAOLive().getAllQuizzesByTopic(topic);
        }

        // Display the quizzes
        for (Quiz quiz : quizzes) {
            VBox card = new VBox();
            card.setSpacing(5);
            card.setStyle("-fx-background-color: #F2F2F2; -fx-padding: 10; -fx-background-radius: 10; -fx-cursor: hand");
            card.setPrefWidth(200);
            card.setPrefHeight(150);

            Label title = new Label(quiz.getQuizName());
            title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
            Label topic = new Label("Topic: " + quiz.getTopic());
            Label difficulty = new Label("Difficulty: " + quiz.getDifficulty());
            String scoreText = new SQLiteQuizAttemptDAOLive().getScoreForQuiz(quiz.getQuizID());
            Label result = new Label("Last Score: " + scoreText);
            card.getChildren().addAll(title, topic, difficulty, result);

            // Open confirmation alert when card clicked
            card.setOnMouseClicked(event -> {
                // Create confirmation alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(quiz.getQuizName());
                alert.setHeaderText(null);
                alert.setContentText("What would you like to do?");

                // Ensure image path is correct, consider if it's always available
                Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/images/tutorworm-default.png")).toString());
                ImageView imageView = new ImageView();
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
                imageView.setImage(image);
                alert.setGraphic(imageView);
                Stage confirm_window = (Stage) alert.getDialogPane().getScene().getWindow();
                confirm_window.getIcons().add(image);

                // Create custom button types
                ButtonType takeQuizButton = new ButtonType("Take Quiz", ButtonBar.ButtonData.OK_DONE);
                ButtonType deleteQuizButton = new ButtonType("Delete Quiz", ButtonBar.ButtonData.OTHER);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                // Set the button types for the alert
                alert.getButtonTypes().setAll(takeQuizButton, deleteQuizButton, cancelButton);

                // Show the alert and wait for a button to be pressed
                Optional<ButtonType> input = alert.showAndWait();

                // Process the result
                if (input.isPresent() && input.get() == takeQuizButton) {
                    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("questions-view.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(loader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    QuestionsController controller = loader.getController();
                    controller.setQuiz(quiz);

                    Stage stage = (Stage) card.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Quiz");
                } else if (input.isPresent() && input.get() == deleteQuizButton) {
                    new SQLiteQuizDAOLive().deleteQuiz(quiz);

                    // After deleting, refresh the display to reflect the change
                    // This will also re-filter based on the current topic
                    refreshQuizzesDisplay();
                } else {
                    System.out.println("Cancelled for: " + quiz.getQuizName());
                    // User clicked Cancel or closed the dialog
                }
            });

            quizHistoryBox.getChildren().add(card);
        }
    }

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
            topicDropdown.getItems().addFirst("All Topics");
            topicDropdown.getItems().addAll(topics);
        }

        refreshQuizzesDisplay();

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
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("progress-report-view.fxml"));
                    Scene progressReportPage = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
                    ProgressReportController controller = fxmlLoader.getController();
                    controller.setQuizTopic(selectedTopic);
                    controller.setPreviousScene(viewProgressBtn.getScene(), "Dashboard");
                    Stage stage = (Stage) viewProgressBtn.getScene().getWindow();
                    stage.setScene(progressReportPage);
                    stage.setTitle("Progress Report");

                    Stage loadingStage = QuizAppAlert.loadingSpinner("Generating Feedback...", viewProgressBtn);
                    loadingStage.show();

                    new Thread(() -> {
                        List<QuizAttempt> quizAttempts = new SQLiteQuizAttemptDAOLive().getQuizAttemptsByTopic(topic);
                        controller.setCommentsAreaText(generateFeedback(quizAttempts));
                        Platform.runLater(() -> {
                            loadingStage.close();
                        });
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        setUsername();
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

    public void setUsername() {
        String currentUsername = CurrentUser.getInstance().getUserName();
        userNameLabel.setText(currentUsername);
    }
}
