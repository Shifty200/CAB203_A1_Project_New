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


/**
 * A class for interacting with quiz attempts and the database
 */
public class SQLiteQuizAttemptDAOLive {
    private Connection connection;

    /**
     * Initialises the database connection
     */
    public SQLiteQuizAttemptDAOLive() {
        connection = SQLiteUserConnectionLive.getInstance();
        createTable();
    }

    /**
     * Method that creates a quiz_attempts table in the database if one is not already present.
     */
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

    /**
     * Adds a quiz attempt to the quiz_attempts table in the database.
     * @param quizAttempt the quizAttempt object to add to the database
     */
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

    /**
     * Updates a quiz attempt in the database.
     * @param quizAttempt the quiz attempt to update the database with
     */
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

    /**
     * Deletes a quiz attempt from the database.
     * @param quizAttempt the quiz attempt to delete
     */
    public void deleteQuizAttempt(QuizAttempt quizAttempt) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM quiz_attempts WHERE id = ?");
            statement.setInt(1, quizAttempt.getQuizAttemptID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a quiz attempt from the database using its ID.
     * @param quiz_attempt_id the quizAttempt ID for the quiz attempt wanting to be retrieved
     * @return the quizAttempt object that matches the supplied ID.
     */
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

    /**
     * Retrieves all quiz attempts.
     * @return all quiz attempts in a list.
     */
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

    /**
     * Returns all quiz attempts on a specific topic.
     * @param topic the title of the topic
     * @return A list of all quiz attempts for the requested topic.
     */
    public List<QuizAttempt> getQuizAttemptsByTopicByCurrentUser(String topic) {
        List<QuizAttempt> quiz_attempts = new ArrayList<>();
        String currentUserName = CurrentUser.getInstance().getUserName();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT q.topic, qa.* " +
                            "FROM quiz_attempts qa " +
                            "JOIN quizzes q " +
                            "ON qa.quiz_id = q.quiz_id " +
                            "WHERE qa.userName = ? AND q.topic = ? " +
                            "ORDER BY q.topic;"
            );

            statement.setString(1, currentUserName);
            statement.setString(2, topic);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if(!Objects.equals(resultSet.getString("topic"), topic)){
                    continue;
                }
                int quiz_id = resultSet.getInt("quiz_id");
                String selected_answers = resultSet.getString("selected_answers");

                Quiz quiz = new SQLiteQuizDAOLive().getQuiz(quiz_id);
                QuizAttempt quizAttempt = new QuizAttempt(quiz);

                int[] numbers = QuizAppUtil.fromString(selected_answers);
                quizAttempt.setSelectedAnswers(numbers);
                quiz_attempts.add(quizAttempt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quiz_attempts;
    }


    /**
     * Gets a score for quiz attempt.
     * @param quizId The ID of the quiz to retrieve the score for
     * @return a String in the format "correct/total" (e.g. 7/10) representing the number of correct answers out of total questions, or "Not attempted" if there is no quiz.
     */
    public String getScoreForQuiz(int quizId) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT selected_answers FROM quiz_attempts WHERE quiz_id = ? ORDER BY id DESC LIMIT 1"
            );
            statement.setInt(1, quizId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String selectedAnswers = resultSet.getString("selected_answers");
                Quiz quiz = new SQLiteQuizDAOLive().getQuiz(quizId);
                if (quiz == null) return "Not attempted";

                QuizAttempt attempt = new QuizAttempt(quiz);
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(selectedAnswers);
                int[] selections = new int[quiz.getLength()];
                int i = 0;
                while (matcher.find() && i < selections.length) {
                    selections[i++] = Integer.parseInt(matcher.group());
                }
                attempt.setSelectedAnswers(selections);
                int correct = attempt.getScore();
                int total = quiz.getLength();
                return correct + "/" + total;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Not attempted";
    }

}

