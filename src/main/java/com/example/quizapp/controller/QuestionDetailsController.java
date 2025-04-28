package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.QuizAttempt;
import com.example.quizapp.model.QuizQuestion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER;

public class QuestionDetailsController {
    @FXML
    private Label quizName;
    @FXML
    private Button backToResultsButton;
    @FXML
    private ScrollPane questions;

    private QuizAttempt currentAttempt = QuestionsController.getQuizAttempt();

    @FXML
    private void initialize() {

        // Code to display quiz name
        String name = currentAttempt.getQuiz().getQuizName();
        quizName.setText(name);

        // Code to display list of questions for this current quiz attempt

        // Get list of questions for current quiz attempt
        ArrayList<QuizQuestion> questionsList = currentAttempt.getQuiz().getQuestions();

        // Set up scroll pane to only scroll vertically as needed
        questions.setHbarPolicy(NEVER);
        questions.setVbarPolicy(AS_NEEDED);
        questions.setMaxHeight(500);

        // Create container to group all questions
        VBox allQuestions = new VBox();

        // Loop over list of questions
        for (int i = 0; i < questionsList.toArray().length; i++) {

            QuizQuestion currentQuestion = currentAttempt.getQuiz().getQuestion(i);
            VBox questionContainer = new VBox();
            VBox answerContainer = new VBox();
            HBox resultsContainer = new HBox();

            // Display and access question number and text for current question
            Label questionNumber = new Label("Question" + (i + 1));
            Label question = new Label(currentQuestion.getQuestionText());

            // Get index for the selected answer and correct answer
            int yourAnswer = currentAttempt.getSelectedAnswer(i);
            int correctAnswer = currentQuestion.getCorrectAnswer();

            // Loop over answers for the current question
            for (int j = 0; j < currentQuestion.getAnswersCount(); j ++) {

                // Display radio button with answer text
                RadioButton answerOption = new RadioButton(answerLetter(j) + ". " + currentQuestion.getAnswer(j));

                // Mark the selected answer and correct answer
                answerOption.setSelected(j == yourAnswer || j == correctAnswer);

                // Disable radio buttons and display them normally (without default faded look)
                answerOption.setDisable(true);
                answerOption.setStyle("-fx-opacity: 1");
                answerOption.setPadding(new Insets(10, 0, 10, 0));

                // Group all answers into a container for each question
                answerContainer.getChildren().add(answerOption);
            }

            // Display selected and correct answers in text
            Label yourAnswerLabel = new Label("Your answer: " + answerLetter(yourAnswer));
            Label correctAnswerLabel = new Label("Correct answer: " + answerLetter(correctAnswer));

            // Display horizontally instead of vertically
            resultsContainer.getChildren().addAll(yourAnswerLabel, correctAnswerLabel);
            resultsContainer.setSpacing(10);

            // Group each questions into a container
            questionContainer.getChildren().addAll(questionNumber, question, answerContainer, resultsContainer);
            questionContainer.setSpacing(10);

            // Group all questions into one container
            allQuestions.getChildren().addAll(questionContainer);
            allQuestions.setSpacing(20);
            allQuestions.setPadding(new Insets(10, 10, 10, 10));
        }

        // Set larger container as content of scroll pane
        questions.setContent(allQuestions);
    }

    @FXML
    private void backToResults() throws IOException {
        Stage stage = (Stage) backToResultsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("results-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    // For a multiple choice question, returns the corresponding letter for the answer (starts from 1)
    private String answerLetter(int answerIndex) {

        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        return Character.toString(alphabet[answerIndex]);
    }
}
