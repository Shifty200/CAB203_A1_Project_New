package com.example.quizapp.model;

import com.example.quizapp.controller.DashboardController;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *A class containing methods for interacting with quizzes and the database
 */
public class SQLiteQuizDAOLive{
    private Connection connection;

    /**
     * Initialises the database connection
     */
    public SQLiteQuizDAOLive() {
        connection = SQLiteUserConnectionLive.getInstance();
        createTable();
    }
    /**
     * Method that creates a "quizzes" table in the database if one is not already present.
     */
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

    /**
     * Adds a quiz to the database.
     * @param quiz The quiz object to add to the quizzes table.
     */
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

    /**
     * A method to update a quiz in the database.
     * @param quiz The quiz object to update the database with
     */
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

    /**
     * Deletes a quiz from the database.
     * @param quiz The quiz object to be deleted.
     */
    public void deleteQuiz(Quiz quiz) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM quizzes WHERE quiz_id = ?");
            statement.setInt(1, quiz.getQuizID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves a quiz from the database using its ID.
     * @param quiz_id the quiz ID for the quiz wanting to be retrieved
     * @return the quiz object that matches the supplied ID.
     */
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

                List<QuizQuestion> quiz_questions = new SQLiteQuizQuestionDAOLive().getQuizQuestionsByQuizId(quiz_id);
                quiz.setQuestions((ArrayList<QuizQuestion>)quiz_questions);
                return quiz;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of all quizzes in the database.
     * @return the list of quizzes
     */
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
                quiz.setQuestions(new SQLiteQuizQuestionDAOLive().getQuizQuestionsByQuizId(id));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(quizzes);
    }
    /**
     * Returns all quiz topics made by the current user.
     * @return A list of all the user's topics
     */
    public List<String> getAllTopicsByCurrentUser() {
        List<String> topics = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT q.* " +
                    "FROM quizzes q " +
                    "JOIN quiz_attempts qa ON q.quiz_id = qa.quiz_id " +
                    "WHERE qa.userName = ?;");
            statement.setString(1, CurrentUser.getInstance().getUserName());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String topic = resultSet.getString("topic");
                if (!topics.contains(topic)){
                    topics.add(topic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(topics);
    }

    /**
     * Deletes a topic from the user's topics and all the quizzes related to that topic in the database.
     * @param topic The name of the topic to delete for the current user
     */
    public void deleteTopicAndRelatedQuizzes(String topic) {
        try {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM quizzes WHERE topic = '" + topic + "'";
            statement.executeUpdate(query);
            statement.close();
            DashboardController.displayMessagePopup(Alert.AlertType.INFORMATION, "Deletion Complete",
                    "Deleted quizzes for topic: '" + topic + "'");
        } catch (Exception e) {
            e.printStackTrace();
            DashboardController.displayMessagePopup(Alert.AlertType.ERROR, "Deletion Failed", "Error deleting quizzes: " + e.getMessage());
        }
    }

    public List<Quiz> getAllQuizzesByTopic(String selectedTopic) {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT quiz_id, quizName, topic, difficulty FROM quizzes WHERE topic = '" + selectedTopic + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("quiz_id");
                String quizName = resultSet.getString("quizName");
                String topic = resultSet.getString("topic");
                String difficulty = resultSet.getString("difficulty");
                Quiz quiz = new Quiz(quizName, topic, difficulty);
                quiz.setQuizID(id);
                quiz.setQuestions(new SQLiteQuizQuestionDAOLive().getQuizQuestionsByQuizId(id));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    /**
     * Returns a list of all the quizzes that the user has attempted
     * @return a list of quiz objects including their questions.
     */
    public List<Quiz> getAllQuizzesByCurrentUser() {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT q.* " +
                    "FROM quizzes q " +
                    "JOIN quiz_attempts qa ON q.quiz_id = qa.quiz_id " +
                    "WHERE qa.userName = ?;");
            statement.setString(1, CurrentUser.getInstance().getUserName());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("quiz_id");
                String quizName = resultSet.getString("quizName");
                String topic = resultSet.getString("topic");
                String difficulty = resultSet.getString("difficulty");
                Quiz quiz = new Quiz(quizName, topic, difficulty);
                quiz.setQuizID(id);
                quiz.setQuestions(new SQLiteQuizQuestionDAOLive().getQuizQuestionsByQuizId(id));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(quizzes);
    }

    /**
     * Checks if a topic already exists and adds it to the user's topics if it doesn't.
     * @param topic the name of the topic to add
     * @return True if a topic does not exist and creates the topic in the database for the user. If the topic already exists, or there was an error, it returns False.
     */
    public boolean insertNewTopicIfNotExists(String topic) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM quizzes WHERE topic = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, topic);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // topic already exists
            }

            Quiz placeholderQuiz = new Quiz("Placeholder Quiz", topic);
            placeholderQuiz.addQuestion(new QuizQuestion());
            QuizAttempt placeholderAttempt = new QuizAttempt(placeholderQuiz);
            addQuiz(placeholderQuiz);
            new SQLiteQuizAttemptDAOLive().addQuizAttempt(placeholderAttempt);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

