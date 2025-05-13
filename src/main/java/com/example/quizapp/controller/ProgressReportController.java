package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.model.QuizAttempt;
import com.example.quizapp.model.QuizQuestion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom; // remove later

public class ProgressReportController {
    @FXML
    private Label quizTopicLabel;
    @FXML
    private LineChart lineChart;
    @FXML
    private NumberAxis attemptNumAxis;
    @FXML
    private TextArea commentsArea;
    @FXML
    private Button backButton;
    @FXML
    private Button dashboardButton;

    public void initialize() {
        commentsArea.setText("""
Lorem ipsum dolor sit amet, consectetur adipiscing elit,
sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
Eget dolor morbi non arcu risus. Quis lectus nulla at volutpat diam
ut venenatis tellus in. Feugiat in fermentum posuere urna nec tincidunt
praesent semper. Turpis tincidunt id aliquet risus feugiat in.
Libero volutpat sed cras ornare. Facilisi morbi tempus iaculis urna.
Bibendum est ultricies integer quis auctor. Eu augue ut lectus arcu.
Tincidunt tortor aliquam nulla facilisi cras fermentum odio eu.
Gravida neque convallis a cras. Elit ut aliquam purus sit.
Suspendisse ultrices gravida dictum fusce ut placerat.
Integer feugiat scelerisque varius morbi enim nunc.
Amet justo donec enim diam vulputate ut pharetra.
Sapien pellentesque habitant morbi tristique.
Lorem sed risus ultricies tristique nulla aliquet.
Elementum nibh tellus molestie nunc non blandit massa.""");

        setLineChartData(generateQuizAttempts(10));
    }

    public void setQuizTopicLabel(String topic) {
        quizTopicLabel.setText("Progress Report: " + topic);
    }

    public void setCommentsAreaText(String comments) {
        commentsArea.setText(comments);
    }

    public void setLineChartData(QuizAttempt[] quizAttempts) {
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < quizAttempts.length; i++) {
            series.getData().add(new XYChart.Data(i + 1, quizAttempts[i].getScorePercentage()));
        }
        attemptNumAxis.setUpperBound(quizAttempts.length);
        lineChart.getData().add(series);
    }

    @FXML
    private void onBackButtonPressed() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("results-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Quiz Results");
    }

    @FXML
    private void onDashboardButtonPressed() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/quizapp/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        Stage stage = (Stage) dashboardButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dashboard");
    }

    // generates an array of quiz attempts for testing -- delete later
    public static QuizAttempt[] generateQuizAttempts(int numAttempts) {
        QuizAttempt[] quizAttempts = new QuizAttempt[numAttempts];
        Quiz quiz = new Quiz();
        for (int i = 0; i < 4; i++) {
            quiz.addQuestion(new QuizQuestion());
        }
        for (int i = 0; i < quizAttempts.length; i++) {
            QuizAttempt attempt = new QuizAttempt(quiz);
            for (int j = 0; j < attempt.getSelectedAnswers().length; j++) {
                attempt.setSelectedAnswer(j, ThreadLocalRandom.current().nextInt(-1,
                        attempt.getQuiz().getQuestion(j).getAnswersCount()));
            }
            quizAttempts[i] = attempt;
        }
        return quizAttempts;
    }
}
