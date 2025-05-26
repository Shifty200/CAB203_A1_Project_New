package com.example.quizapp.controller;

import com.example.quizapp.HelloApplication;
import com.example.quizapp.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom; // remove later

import static com.example.quizapp.model.AIFeedbackGenerator.generateFeedback;


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

    private String topic;
    private Scene previousScene;
    private String previousPage;

    public void initialize() {
        List<QuizAttempt> quizAttempts = new SQLiteQuizAttemptDAOLive().getQuizAttemptsByTopic(topic);
        setCommentsAreaText(generateFeedback(quizAttempts));
    }

    // must be called before switching to this page
    public void setQuizTopic(String topic) {
        this.topic = topic;
        quizTopicLabel.setText("Progress Report: " + topic);
        setLineChartData(new SQLiteQuizAttemptDAOLive().getQuizAttemptsByTopic(topic));
    }

    // must be called before switching to this page
    public void setPreviousScene(Scene scene, String page) {
        this.previousScene = scene;
        this.previousPage = page;
    }

    public void setCommentsAreaText(String comments) {
        commentsArea.setText(comments);
    }

    public void setLineChartData(List<QuizAttempt> quizAttempts) {
        QuizAttempt[] array = new QuizAttempt[quizAttempts.size()];
        array = quizAttempts.toArray(array);
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < array.length; i++) {
            series.getData().add(new XYChart.Data(i + 1, array[i].getScorePercentage()));
        }
        attemptNumAxis.setUpperBound(quizAttempts.size());
        lineChart.getData().add(series);
    }

    @FXML
    private void onBackButtonPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(previousScene);
        stage.setTitle(previousPage);
    }

    @FXML
    private void onDashboardButtonPressed() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/quizapp/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        Stage stage = (Stage) dashboardButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dashboard");
    }

}
