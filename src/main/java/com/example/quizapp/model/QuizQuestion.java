package com.example.quizapp.model;

import java.util.*;

/**
 * A class representing a single quiz question and its list of possible answers.
 */
public class QuizQuestion {
    private String questionText;
    private ArrayList<String> answers;
    private int correctAnswer; // index of correct answer in answers list
    private int questionID;
    private Quiz quiz;

    /**
     * Constructs a new quiz question with default values.
     */
    public QuizQuestion() {
        this.questionText = "Question Text";
        this.answers = new ArrayList<>(Arrays.asList("Option A", "Option B", "Option C", "Option D"));
        this.correctAnswer = 0;
    }
    /**
     * Constructs a new quiz question with provided text, answers list, and correct answer.
     * @param questionText The text forming the question.
     * @param answers The list of possible answers.
     * @param correctAnswer The index of the correct answer in the list.
     */
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

    /**
     * Returns the question's text.
     * @return The question text.
     */
    public String getQuestionText() {
        return questionText;
    }
    /**
     * Sets the text for the question.
     * @param questionText The string to set the question text to.
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Returns the question's ID.
     * @return The question ID.
     */
    public int getQuestionID() {return this.questionID;}
    /**
     * Sets the ID of the question.
     * @param questionID The ID to assign to the question.
     */
    public void setQuestionID(int questionID) {this.questionID = questionID;}

    /**
     * Returns the Quiz object that the question belongs to.
     * @return The Quiz object.
     */
    public Quiz getQuiz() {return this.quiz;}
    /**
     * Assigns the question to a Quiz object.
     * @param quiz The Quiz object to assign the question to.
     */
    public void setQuiz(Quiz quiz) {this.quiz = quiz;}

    /**
     * Returns the list of possible answers for the question.
     * @return The list of answers.
     */
    public List<String> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
    /**
     * Sets the list of possible answers for the question.
     * @param answers The list of answers.
     */
    public void setAnswers(ArrayList<String> answers) throws IllegalArgumentException {
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("Provided 'answers' list is empty. QuizQuestion must have at least one answer.");
        } else {
            this.answers = answers;
        }
    }

    /**
     * Returns the answer at the provided index.
     * @param answerIndex The index of the answer to be returned.
     * @return The answer text.
     */
    public String getAnswer(int answerIndex) {
        return answers.get(answerIndex);
    }
    /**
     * Sets the answer at the provided index.
     * @param answerIndex The index of the answer to be changed.
     * @param answerText The new answer text.
     */
    public void setAnswer(int answerIndex, String answerText) {
        answers.set(answerIndex, answerText);
    }

    /**
     * Returns the index of the correct answer for the question.
     * @return The correct answer's index.
     */
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    /**
     * Sets the index of the correct answer within the list of possible answers.
     * @param correctAnswer The index of the correct answer.
     */
    public void setCorrectAnswer(int correctAnswer) {
        if (correctAnswer >= answers.size() || correctAnswer < 0) {
            throw new IndexOutOfBoundsException("Provided 'correctAnswer' index is not within range of 'answers' list.");
        } else {
            this.correctAnswer = correctAnswer;
        }
    }

    /**
     * Returns the number of possible answers for the question.
     * @return The number of answers.
     */
    public int getAnswersCount() {
        return answers.size();
    }

    /**
     * Checks if a provided answer index corresponds to the correct answer.
     * @param selectedIndex The index of the answer to check.
     * @return true if the provided index matches the correct answer index, false otherwise.
     */
    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctAnswer;
    }

    /**
     * Checks if the QuizQuestion object is equal to another object.
     * @param o The object to compare the QuizQuestion to.
     * @return true if the QuizQuestion and the other object are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuizQuestion question)) return false;
        return getCorrectAnswer() == question.getCorrectAnswer()
                && Objects.equals(getQuestionText(), question.getQuestionText())
                && Objects.equals(getAnswers(), question.getAnswers());
    }

    /**
     * Generates a hash code for the quiz question.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getQuestionText(), getAnswers(), getCorrectAnswer());
    }
}
