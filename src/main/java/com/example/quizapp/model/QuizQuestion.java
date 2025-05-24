package com.example.quizapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizQuestion {
    private String questionText;
    private ArrayList<String> answers;
    private int correctAnswer; // index of correct answer in answers list
    private int questionID;
    private Quiz quiz;

    public QuizQuestion() {
        this.questionText = "Question Text";
        this.answers = new ArrayList<>(Arrays.asList("Option A", "Option B", "Option C", "Option D"));
        this.correctAnswer = 0;
    }
    public QuizQuestion(String questionText, ArrayList<String> answers, int correctAnswer)
            throws IndexOutOfBoundsException, IllegalArgumentException {
        this.questionText = questionText;
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("Provided 'answers' list is empty. QuizQuestion must have at least one answer.");
        } else {
            this.answers = answers;
        }
        if (correctAnswer >= answers.size() || correctAnswer < 0) {
            throw new IndexOutOfBoundsException("Provided 'correctAnswer' index is not within range of provided 'answers' list.");
        } else {
            this.correctAnswer = correctAnswer;
        }
    }

    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getQuestionID() {return this.questionID;}
    public void setQuestionID(int questionID) {this.questionID = questionID;}

    public Quiz getQuiz() {return this.quiz;}
    public void setQuiz(Quiz quiz) {this.quiz = quiz;}

    public List<String> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
    public void setAnswers(ArrayList<String> answers) throws IllegalArgumentException {
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("Provided 'answers' list is empty. QuizQuestion must have at least one answer.");
        } else {
            this.answers = answers;
        }
    }

    public String getAnswer(int answerIndex) {
        return answers.get(answerIndex);
    }
    public void setAnswer(int answerIndex, String answerText) {
        answers.set(answerIndex, answerText);
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(int correctAnswer) {
        if (correctAnswer >= answers.size() || correctAnswer < 0) {
            throw new IndexOutOfBoundsException("Provided 'correctAnswer' index is not within range of 'answers' list.");
        } else {
            this.correctAnswer = correctAnswer;
        }
    }

    public int getAnswersCount() {
        return answers.size();
    }


    // added a isCorrect class for testing purposes - can remove further down the line

    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctAnswer;
    }
}
