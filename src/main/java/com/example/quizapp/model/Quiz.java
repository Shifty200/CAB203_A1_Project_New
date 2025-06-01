package com.example.quizapp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a quiz and its properties, including name, topic, difficulty, and question list.
 */
public class Quiz {
    private String quizName;
    private String topic;
    private String difficulty;
    private int quiz_id;
    private ArrayList<QuizQuestion> questions = new ArrayList<>();

    /**
     * Constructs a new Quiz with default name, topic, and difficulty.
     */
    public Quiz() {
        this.quizName = "Quiz Name";
        this.topic = "Quiz Topic";
        this.difficulty = "easy";
    }
    /**
     * Constructs a new Quiz with the provided name, default topic, and default difficulty.
     * @param quizName The name (title) of the quiz.
     */
    public Quiz(String quizName) {
        this.quizName = quizName;
        this.topic = "Quiz Topic";
        this.difficulty = "easy";
    }
    /**
     * Constructs a new Quiz with the provided name, provided topic, and default difficulty.
     * @param quizName The name (title) of the quiz.
     * @param topic The topic of the quiz.
     */
    public Quiz(String quizName, String topic) {
        this.quizName = quizName;
        this.topic = topic;
        this.difficulty = "easy";
    }

    /**
     * Constructs a new Quiz with the provided name, topic, and difficulty.
     * @param quizName The name (title) of the quiz.
     * @param topic The topic of the quiz.
     * @param difficulty The difficulty of the quiz: must be "easy", "medium", or "hard".
     */
    public Quiz(String quizName, String topic, String difficulty) throws IllegalArgumentException {
        this.quizName = quizName;
        this.topic = topic;
        if (!Objects.equals(difficulty, "easy") && !Objects.equals(difficulty, "medium") && !Objects.equals(difficulty, "hard")) {
            throw new IllegalArgumentException(
                    "Unknown difficulty: '" + difficulty + "'. Difficulty must be 'easy', 'medium', or 'hard'."
            );
        } else {
            this.difficulty = difficulty;
        }
    }

    /**
     * Returns the quiz's id.
     * @return The quiz id.
     */
    public int getQuizID() {return quiz_id;}
    /**
     * Sets the quiz's id.
     * @param id The id to assign to the quiz.
     */
    public void setQuizID(int id) { this.quiz_id = id;}

    /**
     * Returns the quiz's name.
     * @return The quiz name.
     */
    public String getQuizName() {
        return quizName;
    }
    /**
     * Sets the quiz's name.
     * @param quizName The name to assign to the quiz.
     */
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    /**
     * Returns the quiz's topic.
     * @return The topic.
     */
    public String getTopic() {
        return topic;
    }
    /**
     * Sets the quiz's topic.
     * @param topic The topic to assign to the quiz.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Returns the quiz's difficulty.
     * @return The difficulty.
     */
    public String getDifficulty() {
        return difficulty;
    }
    /**
     * Sets the quiz's difficulty.
     * @param difficulty The difficulty to assign to the quiz.
     */
    public void setDifficulty(String difficulty) throws IllegalArgumentException {
        if (!Objects.equals(difficulty, "easy") && !Objects.equals(difficulty, "medium") && !Objects.equals(difficulty, "hard")) {
            throw new IllegalArgumentException(
                    "Unknown difficulty: '" + difficulty + "'. Difficulty must be 'easy', 'medium', or 'hard'."
            );
        } else {
            this.difficulty = difficulty;
        }
    }

    /**
     * Returns all the questions in the quiz.
     * @return A list of all the QuizQuestion objects associated with the quiz.
     */
    public List<QuizQuestion> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
    /**
     * Sets the quiz's list of questions.
     * @param questions The list of QuizQuestion objects to assign to the quiz.
     */
    public void setQuestions(ArrayList<QuizQuestion> questions) {
        for (QuizQuestion question : questions) {
            question.setQuiz(this);
        }
        this.questions = questions;
    }

    /**
     * Returns a single QuizQuestion object associated with the quiz.
     * @param questionIndex The index of the QuizQuestion to return.
     * @return The QuizQuestion object.
     */
    public QuizQuestion getQuestion(int questionIndex) {
        return questions.get(questionIndex);
    }
    /**
     * Sets one question in the quiz.
     * @param questionIndex The index of the question to be changed.
     * @param question The new QuizQuestion object.
     */
    public void setQuestion(int questionIndex, QuizQuestion question) {
        question.setQuiz(this);
        questions.set(questionIndex, question);
    }

    /**
     * Adds a question to the end of the list.
     * @param question The QuizQuestion object to add to the quiz.
     */
    public void addQuestion(QuizQuestion question) {
        question.setQuiz(this);
        questions.add(question);
    }

    /**
     * Returns the number of questions in the quiz.
     * @return The quiz length.
     */
    public int getLength() {
        return questions.size();
    }

    /**
     * Checks if the Quiz object is equal to another object.
     * @param o The object to compare the Quiz to.
     * @return true if the quiz and the other object are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quiz quiz)) return false;
        if (!(Objects.equals(getQuizName(), quiz.getQuizName())
                && Objects.equals(getTopic(), quiz.getTopic())
                && Objects.equals(getDifficulty(), quiz.getDifficulty())
                && getLength() == quiz.getLength())) {
            return false;
        }
        for (int i = 0; i < getLength(); i++) {
            if (!Objects.equals(getQuestion(i), quiz.getQuestion(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a hash code for the quiz.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getQuizName(), getTopic(), getDifficulty(), getQuestions());
    }
}
