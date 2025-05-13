package com.example.quizapp.model;

import java.util.ArrayList;
import java.util.Objects;

public class Quiz {
    private String quizName;
    private String topic;
    private String difficulty;
    private int quiz_id;
    private ArrayList<QuizQuestion> questions = new ArrayList<>();

    public Quiz() {
        this.quizName = "Quiz Name";
        this.topic = "Quiz Topic";
        this.difficulty = "easy";
    }
    public Quiz(String quizName) {
        this.quizName = quizName;
        this.topic = "Quiz Topic";
        this.difficulty = "easy";
    }
    public Quiz(String quizName, String topic) {
        this.quizName = quizName;
        this.topic = topic;
        this.difficulty = "easy";
    }

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

    public int getQuizID() {return quiz_id;}
    public void setQuizID(int id) { this.quiz_id = id;}

    public String getQuizName() {
        return quizName;
    }
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) throws IllegalArgumentException {
        if (!Objects.equals(difficulty, "easy") && !Objects.equals(difficulty, "medium") && !Objects.equals(difficulty, "hard")) {
            throw new IllegalArgumentException(
                    "Unknown difficulty: '" + difficulty + "'. Difficulty must be 'easy', 'medium', or 'hard'."
            );
        } else {
            this.difficulty = difficulty;
        }
    }

    public ArrayList<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuizQuestion> questions) {
        this.questions = questions;
    }

    public QuizQuestion getQuestion(int questionIndex) {
        return questions.get(questionIndex);
    }

    public void setQuestion(int questionIndex, QuizQuestion question) {
        questions.set(questionIndex, question);
    }

    public void addQuestion(QuizQuestion question) {
        // i added code here to link the quiz question to the quiz
        question.setQuiz(this);
        questions.add(question);
    }

    public int getLength() {
        return questions.size();
    }
}
