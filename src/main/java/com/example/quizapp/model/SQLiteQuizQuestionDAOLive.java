package com.example.quizapp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for interacting with quiz questions and the database.
 */
public class SQLiteQuizQuestionDAOLive {
    private Connection connection;

    /**
     * Initialises the database connection
     */
    public SQLiteQuizQuestionDAOLive() {
        connection = SQLiteUserConnectionLive.getInstance();
        createTable();
    }
    /**
     * Method that creates a "quiz_questions" table in the database if one is not already present.
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quiz_questions ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quiz_id INTEGER NOT NULL,"
                    + "questionText VARCHAR NOT NULL,"
                    + "answerText VARCHAR NOT NULL,"
                    + "correctAnswer INTEGER NOT NULL,"
                    + "FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a quiz question to the database.
     * @param quizQuestion The quizQuestion object to add to the quiz_questions table.
     */
    public void addQuizQuestion(QuizQuestion quizQuestion) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO quiz_questions (quiz_id, questionText, answerText, correctAnswer) VALUES (?, ?, ?, ?)");
            statement.setInt(1, quizQuestion.getQuiz().getQuizID());
            statement.setString(2, quizQuestion.getQuestionText());
            statement.setString(3, String.join(",", quizQuestion.getAnswers()));
            statement.setInt(4, quizQuestion.getCorrectAnswer());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                quizQuestion.setQuestionID(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * A method to update a quiz question in the database.
     * @param quizQuestion The question object to update the database with
     */
    public void updateQuizQuestion(QuizQuestion quizQuestion) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE quiz_questions SET quiz_id = ?, questionText = ?, answerText = ?, correctAnswer WHERE id = ?");
            statement.setInt(1, quizQuestion.getQuiz().getQuizID());
            statement.setString(2, quizQuestion.getQuestionText());
            statement.setString(3, String.join(",", quizQuestion.getAnswers()));
            statement.setInt(4, quizQuestion.getCorrectAnswer());
            statement.setInt(5, quizQuestion.getQuestionID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Deletes a question from the database.
     * @param quizQuestion The quizQuestion object to be deleted.
     */
    public void deleteQuizQuestion(QuizQuestion quizQuestion) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM quiz_questions WHERE id = ?");
            statement.setInt(1, quizQuestion.getQuestionID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a quiz question from the database corresponding to the supplied ID
     * @param quiz_question_id The ID for the quiz question to be returned
     * @return a quizQuestion object that matches the ID given.
     */
    public QuizQuestion getQuizQuestion(int quiz_question_id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM quiz_questions WHERE id = ?");
            statement.setInt(1, quiz_question_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int quiz_id = resultSet.getInt("quiz_id");
                String questionText = resultSet.getString("questionText");
                String answerText = resultSet.getString("answerText");
                int correctAnswer = resultSet.getInt("correctAnswer");
                int quizID = resultSet.getInt("quiz_id");

                ArrayList<String> answers = new ArrayList<String>(List.of(answerText.split("\\s*,\\s*")));

                QuizQuestion quizQuestion = new QuizQuestion(questionText, answers, correctAnswer);
                quizQuestion.setQuiz(new SQLiteQuizDAOLive().getQuiz(quizID));
                return quizQuestion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all quiz questions from the database
     * @return A list of all quiz questions in the database
     */
    public List<QuizQuestion> getAllQuizQuestions() {
        List<QuizQuestion> quiz_questions = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM quiz_questions";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int quiz_id = resultSet.getInt("quiz_id");
                String questionText = resultSet.getString("questionText");
                String answerText = resultSet.getString("answerText");
                int correctAnswer = resultSet.getInt("correctAnswer");
                int quizID = resultSet.getInt("quiz_id");

                ArrayList<String> answers = new ArrayList<String>(List.of(answerText.split("\\s*,\\s*")));

                QuizQuestion quizQuestion = new QuizQuestion(questionText, answers, correctAnswer);
                quizQuestion.setQuiz(new SQLiteQuizDAOLive().getQuiz(quizID));
                quiz_questions.add(quizQuestion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(quiz_questions);
    }

    /**
     * Returns a list of quiz questions that belong to a specific quiz.
     * @param quiz_id The ID of the quiz to retrieve the questions of
     * @return the quiz questions for the corresponding quiz.
     */
    public ArrayList<QuizQuestion> getQuizQuestionsByQuizId(int quiz_id) {
        ArrayList<QuizQuestion> quiz_questions = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM quiz_questions WHERE quiz_id = ?");
            statement.setInt(1, quiz_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String questionText = resultSet.getString("questionText");
                String answerText = resultSet.getString("answerText");
                int correctAnswer = resultSet.getInt("correctAnswer");

                ArrayList<String> answers = new ArrayList<String>(List.of(answerText.split("\\s*,\\s*")));

                QuizQuestion quizQuestion = new QuizQuestion(questionText, answers, correctAnswer);
                quiz_questions.add(quizQuestion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quiz_questions;
    }

}

