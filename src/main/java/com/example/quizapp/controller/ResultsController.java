package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.QuizAppAlert;
import com.example.quizapp.model.QuizAttempt;
import com.example.quizapp.model.QuizQuestion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.example.quizapp.model.AIFeedbackGenerator.generateFeedback;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.*;

public class ResultsController {
    @FXML
    private Label currentResult;
    @FXML
    private PieChart resultGraph;
    @FXML
    private ScrollPane questions;
    @FXML
    private Button retakeQuizButton;
    @FXML
    private Button progressReportButton;
    @FXML
    private Button questionDetailsButton;
    @FXML
    private Button exitButton;

    private QuizAttempt currentAttempt = QuestionsController.getQuizAttempt();

    @FXML
    private void initialize() {

        // Code to display score for this current quiz attempt

        // Access score and quiz length for current quiz attempt
        int score = currentAttempt.getScore();
        int quizLength = currentAttempt.getQuiz().getLength();

        // Display score out of the quiz length
        currentResult.setText(Integer.toString(score) + "/" + Integer.toString(quizLength));

        // Update graph to represent score
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Correct", score),
                new PieChart.Data("Incorrect", quizLength - score));
        resultGraph.setData(pieChartData);
        resultGraph.setLabelsVisible(false);

        // Code to display list of questions for this current quiz attempt

        // Get list of questions for current quiz attempt
        List<QuizQuestion> questionsList = currentAttempt.getQuiz().getQuestions();

        // Set up scroll pane to only scroll vertically as needed
        questions.setHbarPolicy(NEVER);
        questions.setVbarPolicy(AS_NEEDED);
        questions.setMaxHeight(500);

        // Create container to group all questions
        VBox allQuestions = new VBox();

        // Loop over list of questions to display each one
        for (int i = 0; i < questionsList.toArray().length; i++) {

            QuizQuestion currentQuestion = currentAttempt.getQuiz().getQuestion(i);

            VBox questionContainer = new VBox();

            // Access and display question number and text for current question
            Label questionNumber = new Label("Question " + (i + 1));
            questionNumber.setStyle("-fx-font-weight: 700");
            Label question = new Label(currentQuestion.getQuestionText());
            question.setWrapText(true);

            // Group both into a container for each question
            questionContainer.getChildren().addAll(questionNumber, question);
            questionContainer.setSpacing(10);

            // Group all questions into one container
            allQuestions.getChildren().addAll(questionContainer);
            allQuestions.setSpacing(20);
            allQuestions.setPadding(new Insets(10, 10, 10, 10));
        }

        // Set larger container as content of scroll pane
        questions.setContent(allQuestions);

        questions.setHbarPolicy(NEVER);
        questions.setVbarPolicy(AS_NEEDED);
        questions.setMaxHeight(500);
    }

    @FXML
    private void toQuestionDetails() throws IOException {
        Stage stage = (Stage) questionDetailsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("question-details-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setTitle("Question Details");
        stage.setScene(scene);
    }

    @FXML
    private void backToQuiz() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("questions-view.fxml"));
        Scene quizPage = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        QuestionsController controller = fxmlLoader.getController();
        controller.setQuiz(currentAttempt.getQuiz());
        Stage stage = (Stage) retakeQuizButton.getScene().getWindow();
        stage.setScene(quizPage);
        stage.setTitle("Quiz");
    }

    @FXML
    private void toProgressReport() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("progress-report-view.fxml"));
        Scene progressReportPage = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        ProgressReportController controller = fxmlLoader.getController();
        controller.setQuizTopic(currentAttempt.getQuiz().getTopic());
        controller.setPreviousScene(progressReportButton.getScene(), "Results");

        Stage stage = (Stage) progressReportButton.getScene().getWindow();
        stage.setScene(progressReportPage);
        stage.setTitle("Progress Report");

        Stage loadingStage = QuizAppAlert.loadingSpinner("Generating Feedback...", progressReportButton);
        loadingStage.show();

        new Thread(() -> {
            controller.setCommentsAreaText(generateFeedback(currentAttempt));
            Platform.runLater(() -> {
                loadingStage.close();
            });
        }).start();
    }

    @FXML
    private void exitResults() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/quizapp/dashboard.fxml"));
        Scene dashboardStage = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.setScene(dashboardStage);
        stage.setTitle("Dashboard");
    }
}
