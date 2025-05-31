package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.Alert.AlertType.*;

public class DashboardController {

    @FXML private Button settingsButton;
    @FXML private Label userNameLabel;
    @FXML private ComboBox<String> topicDropdown;
    @FXML private Button viewProgressBtn;
    @FXML private Hyperlink logoutLink;
    @FXML private Circle userIcon;
    @FXML private HBox quizHistoryBox;
    @FXML private Button deleteTopicButton;

    private static final Image popupIcon = new Image(Objects.requireNonNull(DashboardController.class.getResource("/com/example/images/tutorworm-default.png")).toString());

    private String topic = "All Topics";
    private final SQLiteQuizDAOLive quizDAO = new SQLiteQuizDAOLive();

    @FXML
    private void handleComboBoxSelection() {
        String selectedValue = topicDropdown.getValue(); // Get the selected value first

        if (Objects.equals(selectedValue, "+ Add New Topic")) {
            // Show default dashboard in the background while prompt open
            topicDropdown.getSelectionModel().select("All Topics");
            this.topic = "All Topics";

            // Show prompt to add topic
            String newTopic = displayAddTopic();

            // If input is not empty, update topic dropdown accordingly, else, reset the dashboard
            if (newTopic != null && !newTopic.trim().isEmpty()) {
                onAddTopicEntered(newTopic);
                this.topic = newTopic;
            } else {
                // If user cancels or enters an empty topic, revert selection to "All Topics"
                this.topic = "All Topics";
            }
            refreshTopicsDisplay(); // Update topics dropdown with the new topic
            refreshQuizzesDisplay(); // Refresh quizzes to show those under new topic
        } else {
            this.topic = selectedValue; // For all other selections, update the topic field
            refreshQuizzesDisplay();
        }
    }

    // Refreshes quizzes displayed based on currently selected topic
    private void refreshQuizzesDisplay() {
        // Clear existing quizzes from the display
        quizHistoryBox.getChildren().clear();

        // Create card to add quiz
        VBox addQuizCard = new VBox();
        addQuizCard.setAlignment(CENTER);
        addQuizCard.setSpacing(5);
        addQuizCard.setPrefWidth(200);
        addQuizCard.setPrefHeight(150);
        addQuizCard.setStyle("-fx-background-color: #F2F2F2; -fx-padding: 10; -fx-background-radius: 10; -fx-cursor: hand;");
        Label plusSymbol = new Label("+");
        Label cardText = new Label("Add New Quiz");
        plusSymbol.setStyle("-fx-font-size: 30;");
        addQuizCard.getChildren().addAll(plusSymbol, cardText);

        quizHistoryBox.getChildren().add(addQuizCard);

        // Navigate to quiz init when card is clicked
        addQuizCard.setOnMouseClicked((MouseEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz-init.fxml"));
                Scene quizInitScene = new Scene(loader.load(), 900, 600);
                Stage stage = (Stage) addQuizCard.getScene().getWindow();
                stage.setScene(quizInitScene);
                stage.setTitle("Quiz Initialisation");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        List<Quiz> quizzes;

        // Display all quizzes if topic not chosen, else, sort by topic
        if (topic == "All Topics") {
            quizzes = quizDAO.getAllQuizzesByCurrentUser();
        }
        else {
            quizzes = quizDAO.getAllQuizzesByTopic(topic);
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
                ImageView imageView = new ImageView();
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
                imageView.setImage(popupIcon);
                alert.setGraphic(imageView);
                Stage confirm_window = (Stage) alert.getDialogPane().getScene().getWindow();
                confirm_window.getIcons().add(popupIcon);

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
                    quizDAO.deleteQuiz(quiz);

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

    // Refreshes topics dropdown box
    private void refreshTopicsDisplay() {

        // Create a ObservableList for ComboBox items
        ObservableList<String> items = FXCollections.observableArrayList();

        // Add both default options and user-defined topics to list
        items.add("All Topics");
        List<String> userDefinedTopics = quizDAO.getAllTopicsByCurrentUser();
        items.addAll(userDefinedTopics);
        items.add("+ Add New Topic");

        // Set list of items to the dropdown (method prevents IndexOutOfBoundsException)
        topicDropdown.setItems(items);

        deleteTopicButton.setVisible(!userDefinedTopics.isEmpty()); // Hide delete button if no topics to delete

        // Ensures that items are fully added before selection attempt (fixes index out of bounds error)
        Platform.runLater(() -> {
            // Set the selected topic to current topic
            if (topicDropdown.getItems().contains(this.topic)) {
                topicDropdown.getSelectionModel().select(this.topic);
            } else {
                // If topic no longer exists, revert to "All Topics"
                topicDropdown.getSelectionModel().select("All Topics");
                this.topic = "All Topics";
            }
        });
    }

    // Displays text input dialog for user to enter a new topic
    private String displayAddTopic() {
        this.topic = "";
        TextInputDialog addTopic = new TextInputDialog();
        addTopic.setTitle("Add Topic");
        addTopic.setHeaderText(null);
        addTopic.setContentText("Please enter a new topic:");
        addTopic.setGraphic(null);

        Stage stage = (Stage) addTopic.getDialogPane().getScene().getWindow();
        stage.getIcons().add(popupIcon);

        // Show the dialog and wait for the user's response
        Optional<String> result = addTopic.showAndWait();

        return result.orElse(null); // Returns the value if present, otherwise null
    }

    // Shows simple message popup
    public static void displayMessagePopup(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setGraphic(null);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(popupIcon);

        alert.showAndWait(); // Show the alert and wait for it to be dismissed
    }

    // Adds new topic to database, displays popup to show if adding was successful or an error occurred
    @FXML
    private void onAddTopicEntered(String topic) {

        boolean success = quizDAO.insertNewTopicIfNotExists(topic);
        if (success) {
            displayMessagePopup(INFORMATION, "Success", "Topic successfully added!");
        } else {
            displayMessagePopup(ERROR, "Error", "Topic failed to add or already exists.");
        }

        this.topic = topic;
    }

    // Deletes topic and related quizzes from the database and refreshes page
    @FXML private void onDeleteTopicPressed() {
        String topicToDelete = topicDropdown.getSelectionModel().getSelectedItem();
        if (topicToDelete != null && !topicToDelete.equals("All Topics") && !topicToDelete.equals("+ Add New Topic")) {
            quizDAO.deleteTopicAndRelatedQuizzes(topicToDelete);
            refreshTopicsDisplay();
            refreshQuizzesDisplay();
        } else {
            displayMessagePopup(Alert.AlertType.WARNING, "Deletion Warning", "Please select a specific topic (not 'All Topics' or '+ Add New Topic') to delete.");
        }
    }

    @FXML
    public void initialize() {

        Image img = new Image(getClass().getResource("/com/example/images/user-icon.png").toString());
        userIcon.setFill(new ImagePattern(img));

        refreshTopicsDisplay();
        refreshQuizzesDisplay();

        viewProgressBtn.setOnAction(event -> {
            String selectedTopic = topicDropdown.getValue();
            if (selectedTopic != null && !selectedTopic.equals("All Topics") && !selectedTopic.equals("+ Add New Topic")) {
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
                        List<QuizAttempt> quizAttempts = new SQLiteQuizAttemptDAOLive().getQuizAttemptsByTopicByCurrentUser(topic);
                        controller.setCommentsAreaText(generateFeedback(quizAttempts));
                        Platform.runLater(() -> {
                            loadingStage.close();
                        });
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                displayMessagePopup(ERROR, "Error", "Please select a topic first to view its progress!");
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
