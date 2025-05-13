package com.example.quizapp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLiteQuizAttemptDAOLive {
    private Connection connection;

    public SQLiteQuizAttemptDAOLive() {
        connection = SQLiteUserConnectionLive.getInstance();
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quiz_attempts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quiz_id INTEGER NOT NULL,"
                    + "selected_answers VARCHAR NOT NULL,"
                    + "userName VARCHAR NOT NULL,"
                    + "FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE,"
                    + "FOREIGN KEY (userName) REFERENCES users(userName) ON DELETE CASCADE"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addQuizAttempt(QuizAttempt quizAttempt) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO quiz_attempts (quiz_id, selected_answers, userName) VALUES (?, ?, ?)");
            statement.setInt(1, quizAttempt.getQuiz().getQuizID());
            statement.setString(2, Arrays.toString(quizAttempt.getSelectedAnswers()));
            statement.setString(3, CurrentUser.getInstance().getUserName());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                quizAttempt.setQuizAttemptID(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQuizAttempt(QuizAttempt quizAttempt) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE quiz_attempts SET quiz_id = ?, selected_answers = ?, userName = ? WHERE id = ?");
            statement.setInt(1, quizAttempt.getQuiz().getQuizID());
            statement.setString(2, Arrays.toString(quizAttempt.getSelectedAnswers()));
            statement.setString(3, CurrentUser.getInstance().getUserName());
            statement.setInt(4, quizAttempt.getQuizAttemptID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQuizAttempt(QuizAttempt quizAttempt) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM quiz_attempts WHERE id = ?");
            statement.setInt(1, quizAttempt.getQuizAttemptID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QuizAttempt getQuizAttempt(int quiz_attempt_id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM quiz_attempts WHERE id = ?");
            statement.setInt(1, quiz_attempt_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int quiz_id = resultSet.getInt("quiz_id");
                String selected_answers = resultSet.getString("selected_answers");

                Quiz quiz = new SQLiteQuizDAOLive().getQuiz(quiz_id);
                QuizAttempt quizAttempt = new QuizAttempt(quiz);

                Matcher matcher = Pattern.compile("\\d+").matcher(selected_answers);
                int[] numbers = new int[quiz.getLength()];
                for (int i=0;i<numbers.length; i++) {
                    while (matcher.find()) {
                        numbers[i] = (Integer.valueOf(matcher.group()));
                    }
                }
                quizAttempt.setSelectedAnswers(numbers);
                return quizAttempt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<QuizAttempt> getAllQuizAttempts() {
        List<QuizAttempt> quiz_attempts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM quiz_attempts";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int quiz_id = resultSet.getInt("quiz_id");
                String selected_answers = resultSet.getString("selected_answers");

                Quiz quiz = new SQLiteQuizDAOLive().getQuiz(quiz_id);
                QuizAttempt quizAttempt = new QuizAttempt(quiz);

                Matcher matcher = Pattern.compile("\\d+").matcher(selected_answers);
                int[] numbers = new int[quiz.getLength()];
                for (int i=0;i<numbers.length; i++) {
                    while (matcher.find()) {
                        numbers[i] = (Integer.valueOf(matcher.group()));
                    }
                }
                quizAttempt.setSelectedAnswers(numbers);
                quiz_attempts.add(quizAttempt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quiz_attempts;
    }

    public List<QuizAttempt> getQuizAttemptsByTopic(String topic) {
        List<QuizAttempt> quiz_attempts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT quizzes.topic, quiz_attempts.* " +
                    "FROM quiz_attempts " +
                    "JOIN quizzes " +
                    "ON quiz_attempts.quiz_id = quizzes.quiz_id " +
                    "ORDER BY quizzes.topic;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if(!Objects.equals(resultSet.getString("topic"), topic)){
                    continue;
                }
                int quiz_id = resultSet.getInt("quiz_id");
                String selected_answers = resultSet.getString("selected_answers");

                Quiz quiz = new SQLiteQuizDAOLive().getQuiz(quiz_id);
                QuizAttempt quizAttempt = new QuizAttempt(quiz);

                Matcher matcher = Pattern.compile("\\d+").matcher(selected_answers);
                int[] numbers = new int[quiz.getLength()];
                for (int i=0;i<numbers.length; i++) {
                    while (matcher.find()) {
                        numbers[i] = (Integer.valueOf(matcher.group()));
                    }
                }
                quizAttempt.setSelectedAnswers(numbers);
                quiz_attempts.add(quizAttempt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quiz_attempts;
    }

//    public boolean checkUserPresent(String userName){
//        List<User> users = getAllUsers();
//        int userLength = users.size();
//        for (int i=0 ; i < userLength; i++){
//            if(Objects.equals(users.get(i).getUserName(), userName)){
//                return true;
//            }
//        }
//        return false;
//
//    }
}

