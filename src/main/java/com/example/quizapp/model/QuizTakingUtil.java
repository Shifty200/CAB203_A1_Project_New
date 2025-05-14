package com.example.quizapp.model;

import java.util.Scanner;

public class QuizTakingUtil {
    // method for taking quiz through CLI (no input validation -- invalid input will cause exception)
    public static void attemptQuizCLI(QuizAttempt attempt) {
        System.out.println(attempt.getQuizName());
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < attempt.getQuiz().getLength(); i++) {
            QuizQuestion currentQ = attempt.getQuiz().getQuestion(i);
            System.out.println(currentQ.getQuestionText());
            for (int j = 0; j < currentQ.getAnswersCount(); j++) {
                System.out.println(j + ". " + currentQ.getAnswer(j));
            }
            System.out.print("Enter answer index: ");
            int input = scanner.nextInt();
            attempt.setSelectedAnswer(i, input);
            if (input == currentQ.getCorrectAnswer()) {
                System.out.println("Correct");
            } else {
                System.out.println("Incorrect");
            }
        }
        System.out.println("Score: " + attempt.getScore() + " out of " + attempt.getQuiz().getLength());
        System.out.println("= " + attempt.getScorePercentage() + "%");
    }

    // returns a quiz with default values with specified number of questions
    public static Quiz generateDefaultQuiz(int numQuestions) {
        Quiz quiz = new Quiz();
        for (int i = 0; i < numQuestions; i++) {
            QuizQuestion question = new QuizQuestion();
            question.setQuiz(quiz);
            quiz.addQuestion(question);
        }
        return quiz;
    }

    // reverse the Array.toString method for an array of ints
    // from https://stackoverflow.com/questions/456367/reverse-parse-the-output-of-arrays-tostringint
    public static int[] fromString(String string) {
        String[] strings = string.replace("[", "").replace("]", "").split(", ");
        int[] result = new int[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }
        return result;
    }
}
