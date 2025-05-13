package com.example.quizapp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteQuizDAOLive{
    private Connection connection;

    public SQLiteQuizDAOLive() {
        connection = SQLiteUserConnectionLive.getInstance();
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quizzes ("
                    + "quiz_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quizName VARCHAR NOT NULL,"
                    + "topic VARCHAR NOT NULL,"
                    + "difficulty VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addQuiz(Quiz quiz) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO quizzes (quizName, topic, difficulty) VALUES (?, ?, ?)");
            statement.setString(1, quiz.getQuizName());
            statement.setString(2, quiz.getTopic());
            statement.setString(3, quiz.getDifficulty());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                quiz.setQuizID(generatedKeys.getInt(1));
            }
            for (int i = 0; i< quiz.getLength(); i++) {
                new SQLiteQuizQuestionDAOLive().addQuizQuestion(quiz.getQuestions().get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateQuiz(Quiz quiz) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE quizzes SET quizName = ?, topic = ?, difficulty = ? WHERE quiz_id = ?");
            statement.setString(1, quiz.getQuizName());
            statement.setString(2, quiz.getTopic());
            statement.setString(3, quiz.getDifficulty());
            statement.setInt(4, quiz.getQuizID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQuiz(Quiz quiz) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM quizzes WHERE quiz_id = ?");
            statement.setInt(1, quiz.getQuizID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // edited this code as the second query was broken
    public Quiz getQuiz(int quiz_id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM quizzes WHERE quiz_id = ?");
            statement.setInt(1, quiz_id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String quizName = resultSet.getString("quizName");
                String topic = resultSet.getString("topic");
                String difficulty = resultSet.getString("difficulty");
                Quiz quiz = new Quiz(quizName, topic, difficulty);

                ArrayList<QuizQuestion> quiz_questions = new ArrayList<>();
                PreparedStatement statement_quiz = connection.prepareStatement(
                        "SELECT * FROM quiz_attempts WHERE quiz_id = ?"
                );
                statement_quiz.setInt(1, quiz_id);
                ResultSet resultSet2 = statement_quiz.executeQuery();

                while (resultSet2.next()) {
                    int quiz_attempt_id = resultSet2.getInt("id");
                    QuizQuestion question = new SQLiteQuizQuestionDAOLive().getQuizQuestion(quiz_attempt_id);
                    if (question != null) {
                        quiz.addQuestion(question);
                    }
                }

                return quiz;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM quizzes";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("quiz_id");
                String quizName = resultSet.getString("quizName");
                String topic = resultSet.getString("topic");
                String difficulty = resultSet.getString("difficulty");
                Quiz quiz = new Quiz(quizName, topic, difficulty);
                quiz.setQuizID(id);
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    public List<String> getAllTopics() {
        List<String> topics = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM quizzes";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String topic = resultSet.getString("topic");
                if (!topics.contains(topic)){
                    topics.add(topic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
    }


}

