package com.example.quizapp.model;
import java.util.Arrays;

/**
 * A class representing a quiz attempt and its properties, including the selected answers and ID for a particular quiz.
 */

public class QuizAttempt {
    private final Quiz quiz;
    private int[] selectedAnswers; // index of selected answer in answers list for each question. -1 means no answer selected.
    private int quizAttemptID;

    /**
     * Constructs a new quiz attempt.
     * @param quiz The quiz being attempted.
     * @throws IllegalArgumentException If provided quiz has no questions.
     */
    public QuizAttempt(Quiz quiz) throws IllegalArgumentException {
        if (quiz.getLength() == 0) {
            throw new IllegalArgumentException("Provided 'quiz' has no questions. Quiz must have at least one question before QuizAttempt can be created.");
        }
        this.quiz = quiz;
        this.selectedAnswers = new int[quiz.getLength()];
        Arrays.fill(selectedAnswers, -1);
    }

    /**
     * Sets the ID of the quiz attempt.
     * @param quizAttemptID An integer representing the quiz attempt ID.
     */
    public void setQuizAttemptID(int quizAttemptID) {this.quizAttemptID = quizAttemptID;}

    /**
     * Retrieves the ID of the quiz attempt.
     * @return Integer representing the quiz attempt ID.
     */
    public int getQuizAttemptID(){return this.quizAttemptID;}

    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * Gets the name of the quiz being attempted.
     * @return A string representing the quiz name.
     */
    public String getQuizName() {
        return quiz.getQuizName();
    }

    /**
     * Gets the selected answers for this quiz attempt.
     * @return An array of integers representing the answers selected by the user for this attempt for each question.
     */
    public int[] getSelectedAnswers() {
        return selectedAnswers;
    }

    /**
     * Sets the selected answers for this quiz attempt.
     * @param selectedAnswers An array of integers representing the answers selected by the user for this attempt for each question.
     */
    public void setSelectedAnswers(int[] selectedAnswers) {this.selectedAnswers = selectedAnswers;}

    /**
     * For a question, get the selected answer.
     * @param questionIndex The index of the question in a list of questions for a quiz.
     * @return An integer representing the selected answer for that question.
     */
    public int getSelectedAnswer(int questionIndex) {
        return selectedAnswers[questionIndex];
    }

    /**
     * For a question, set the selected answer.
     * @param questionIndex The index of the question in a list of questions for a quiz.
     *      * @param selectedAnswer An integer representing the answer selected by the user for the question.
     *      * @throws IndexOutOfBoundsException If the selected answer is not within range of the answers list for the question.
     */
    public void setSelectedAnswer(int questionIndex, int selectedAnswer) throws IndexOutOfBoundsException {
        if (selectedAnswer >= quiz.getQuestion(questionIndex).getAnswersCount() || selectedAnswer < -1) {
            throw new IndexOutOfBoundsException("Provided 'selectedAnswer' index is not within range of answers list for provided 'questionIndex'.");
        } else {
            selectedAnswers[questionIndex] = selectedAnswer;
        }
    }

    /**
     * Get the total number of answered questions for a quiz attempt.
     * @return An integer representing all answered questions for a quiz attempt.
     */
    public int getAnsweredQuestionsCount() {
        int count = 0;
        for (int answer : selectedAnswers) {
            if (answer != -1) count++;
        }
        return count;
    }

    /**
     * Determines whether an answer is correct or incorrect.
     * @param questionIndex The question for which the answer is being checked.
     * @return True if the answer is correct, false if the answer is incorrect.
     */
    public boolean answerIsCorrect(int questionIndex) {
        return selectedAnswers[questionIndex] == quiz.getQuestion(questionIndex).getCorrectAnswer();
    }

    /**
     * Get the final score for the quiz attempt
     * @return An integer representing the total number of correct answers for the quiz attempt.
     */
    public int getScore() {
        int score = 0;
        for (int i = 0; i < quiz.getLength(); i++) {
           if (answerIsCorrect(i)) {
               score++;
           }
        }
        return score;
    }

    /**
     * Calculates the score as a percentage.
     * @return A double representing the score as a percentage value.
     */
    public double getScorePercentage() { // returns score as a percentage to one decimal place
        return Math.round(((double) getScore() / (double) quiz.getLength()) * 1000.0) / 10.0;
    }
}
