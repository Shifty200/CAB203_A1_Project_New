package com.example.quizapp.model;

import java.util.Arrays;

public class QuizAttempt {
    private final Quiz quiz;
    private int[] selectedAnswers; // index of selected answer in answers list for each question. -1 means no answer selected.
    private int quizAttemptID;

    public QuizAttempt(Quiz quiz) throws IllegalArgumentException {
        if (quiz.getLength() == 0) {
            throw new IllegalArgumentException("Provided 'quiz' has no questions. Quiz must have at least one question before QuizAttempt can be created.");
        }
        this.quiz = quiz;
        this.selectedAnswers = new int[quiz.getLength()];
        Arrays.fill(selectedAnswers, -1);
    }

    public void setQuizAttemptID(int quizAttemptID) {this.quizAttemptID = quizAttemptID;}
    public int getQuizAttemptID(){return this.quizAttemptID;}

    public Quiz getQuiz() {
        return quiz;
    }

    public String getQuizName() {
        return quiz.getQuizName();
    }

    public int[] getSelectedAnswers() {
        return selectedAnswers;
    }
    public void setSelectedAnswers(int[] selectedAnswers) {this.selectedAnswers = selectedAnswers;}

    public int getSelectedAnswer(int questionIndex) {
        return selectedAnswers[questionIndex];
    }
    public void setSelectedAnswer(int questionIndex, int selectedAnswer) throws IndexOutOfBoundsException{
        if (selectedAnswer >= quiz.getQuestion(questionIndex).getAnswersCount() || selectedAnswer < -1) {
            throw new IndexOutOfBoundsException("Provided 'selectedAnswer' index is not within range of answers list for provided 'questionIndex'.");
        } else {
            selectedAnswers[questionIndex] = selectedAnswer;
        }
    }

    public int getAnsweredQuestionsCount() {
        int count = 0;
        for (int answer : selectedAnswers) {
            if (answer != -1) count++;
        }
        return count;
    }

    public boolean answerIsCorrect(int questionIndex) {
        return selectedAnswers[questionIndex] == quiz.getQuestion(questionIndex).getCorrectAnswer();
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < quiz.getLength(); i++) {
           if (answerIsCorrect(i)) {
               score++;
           }
        }
        return score;
    }

    public double getScorePercentage() { // returns score as a percentage to one decimal place
        return Math.round(((double) getScore() / (double) quiz.getLength()) * 1000.0) / 10.0;
    }
}
