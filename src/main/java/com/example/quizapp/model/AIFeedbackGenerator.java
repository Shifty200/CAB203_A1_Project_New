package com.example.quizapp.model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class AIFeedbackGenerator {

    /**
     * Generates AI feedback for a list of quiz attempts.
     * The feedback includes a summary for each attempt, and overall study advice.
     * The AI will identify patterns from raw data of incorrect answers.
     *
     * @param quizAttempts A list of QuizAttempt objects to analyze.
     * @return A String containing the AI-generated feedback, or an error message.
     */
    public static String generateFeedback(List<QuizAttempt> quizAttempts) {

        // Return error if list is empty
        if (quizAttempts == null || quizAttempts.isEmpty()) {
            return "No quiz attempts provided for feedback.";
        }

        // Create a prompt for the AI to respond to
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Provide short, insightful feedback for the following quiz attempts. ")
                .append("Summarize each attempt's performance. Identify common patterns, strengths, and weaknesses across all attempts. ")
                .append("Suggest specific areas for improvement or topics to review based on recurring incorrect answers. ")
                .append("Conclude with an overall summary of the user's performance and general study advice.\n\n");

        // List to store details of every incorrect answer for AI to analyze
        List<String> IncorrectQuestionDetails = new ArrayList<>();

        for (int attemptIndex = 0; attemptIndex < quizAttempts.size(); attemptIndex++) {
            QuizAttempt quizAttempt = quizAttempts.get(attemptIndex);
            Quiz quiz = quizAttempt.getQuiz();

            promptBuilder.append("--- Quiz Attempt ").append(attemptIndex + 1).append(" (").append(quiz.getQuizName()).append(") ---\n");

            int correctCount = 0;
            int totalQuestions = quiz.getQuestions().size();

            for (int i = 0; i < totalQuestions; i++) {
                QuizQuestion question = quiz.getQuestion(i);
                int userAnswerIndex = quizAttempt.getSelectedAnswer(i);
                int correctAnswerIndex = question.getCorrectAnswer();

                String userAnswerText = (userAnswerIndex >= 0 && userAnswerIndex < question.getAnswers().size()) ?
                        question.getAnswer(userAnswerIndex) : "[No answer selected or invalid index]";
                String correctAnswerText = question.getAnswer(correctAnswerIndex);
                boolean isCorrect = quizAttempt.answerIsCorrect(i);

                promptBuilder.append("  Q").append(i + 1).append(": ").append(question.getQuestionText()).append("\n")
                        .append("    Your Answer: ").append(userAnswerText).append("\n")
                        .append("    Correct Answer: ").append(correctAnswerText).append("\n")
                        .append("    Status: ").append(isCorrect ? "Correct" : "Incorrect").append("\n");

                if (isCorrect) {
                    correctCount++;
                } else {
                    // Collect details for AI to analyze later
                    IncorrectQuestionDetails.add(
                            "Attempt " + (attemptIndex + 1) + ", Q" + (i + 1) + ": '" + question.getQuestionText() + "'\n" +
                                    "  User chose: '" + userAnswerText + "'\n" +
                                    "  Correct answer was: '" + correctAnswerText + "'"
                    );
                }
            }
            promptBuilder.append("  Score: ").append(correctCount).append("/").append(totalQuestions).append("\n\n");
        }

        // Add all collected incorrect question details for the AI to process
        if (!IncorrectQuestionDetails.isEmpty()) {
            promptBuilder.append("--- Details of All Incorrect Answers ---\n");
            for (String detail : IncorrectQuestionDetails) {
                promptBuilder.append(detail).append("\n\n");
            }
        } else {
            promptBuilder.append("--- All questions answered correctly across all attempts. Excellent! ---\n\n");
        }

        String prompt = promptBuilder.toString();
        String feedback = "";

        try {
            URL url = new URL("http://localhost:11434/api/chat");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject requestJson = new JSONObject()
                    .put("model", "llama3.2:latest")
                    .put("messages", new JSONArray().put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt)))
                    .put("stream", false)
                    .put("options", new JSONObject().put("temperature", 0.7));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestJson.toString().getBytes());
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    sb.append(responseLine);
                }
                JSONObject responseJson = new JSONObject(sb.toString());
                feedback = responseJson.getJSONObject("message").getString("content").trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
            feedback = "Error generating feedback: " + e.getMessage();
        }

        return feedback;
    }

    /**
     * Generates AI feedback for a list of quiz attempts.
     * The feedback includes a summary for each attempt, and overall study advice.
     * The AI will identify patterns from raw data of incorrect answers.
     *
     * @param quizAttempt A single quizAttempt object to analyze.
     * @return A String containing the AI-generated feedback, or an error message.
     */
    public static String generateFeedback(QuizAttempt quizAttempt) {

        // Return error if list is empty
        if (quizAttempt == null) {
            return "No quiz attempt provided for feedback.";
        }

        // Create a prompt for the AI to respond to
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Provide short, insightful feedback for the following quiz attempt. ")
                .append("Summarize the attempt's performance. Identify patterns, strengths, and weaknesses. ")
                .append("Suggest specific areas for improvement or topics to review based on recurring incorrect answers. ")
                .append("Conclude with an overall summary of the user's performance and general study advice.\n\n");

        // List to store details of every incorrect answer for AI to analyze
        List<String> IncorrectQuestionDetails = new ArrayList<>();

        Quiz quiz = quizAttempt.getQuiz();

        promptBuilder.append("--- Quiz Attempt ").append(" (").append(quiz.getQuizName()).append(") ---\n");

        int correctCount = 0;
        int totalQuestions = quiz.getQuestions().size();

        for (int i = 0; i < totalQuestions; i++) {
            QuizQuestion question = quiz.getQuestion(i);
            int userAnswerIndex = quizAttempt.getSelectedAnswer(i);
            int correctAnswerIndex = question.getCorrectAnswer();

            String userAnswerText = (userAnswerIndex >= 0 && userAnswerIndex < question.getAnswers().size()) ?
                    question.getAnswer(userAnswerIndex) : "[No answer selected or invalid index]";
            String correctAnswerText = question.getAnswer(correctAnswerIndex);
            boolean isCorrect = quizAttempt.answerIsCorrect(i);

            promptBuilder.append("  Q").append(i + 1).append(": ").append(question.getQuestionText()).append("\n")
                    .append("    Your Answer: ").append(userAnswerText).append("\n")
                    .append("    Correct Answer: ").append(correctAnswerText).append("\n")
                    .append("    Status: ").append(isCorrect ? "Correct" : "Incorrect").append("\n");

            if (isCorrect) {
                correctCount++;
            } else {
                // Collect details for AI to analyze later
                IncorrectQuestionDetails.add(
                        "Q" + (i + 1) + ": '" + question.getQuestionText() + "'\n" +
                                "  User chose: '" + userAnswerText + "'\n" +
                                "  Correct answer was: '" + correctAnswerText + "'"
                );
            }
        }
        promptBuilder.append("  Score: ").append(correctCount).append("/").append(totalQuestions).append("\n\n");


        // Add all collected incorrect question details for the AI to process
        if (!IncorrectQuestionDetails.isEmpty()) {
            promptBuilder.append("--- Details of All Incorrect Answers ---\n");
            for (String detail : IncorrectQuestionDetails) {
                promptBuilder.append(detail).append("\n\n");
            }
        } else {
            promptBuilder.append("--- All questions answered correctly. Excellent! ---\n\n");
        }

        String prompt = promptBuilder.toString();
        String feedback = "";

        try {
            URL url = new URL("http://localhost:11434/api/chat");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject requestJson = new JSONObject()
                    .put("model", "llama3.2:latest")
                    .put("messages", new JSONArray().put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt)))
                    .put("stream", false)
                    .put("options", new JSONObject().put("temperature", 0.7));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestJson.toString().getBytes());
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    sb.append(responseLine);
                }
                JSONObject responseJson = new JSONObject(sb.toString());
                feedback = responseJson.getJSONObject("message").getString("content").trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
            feedback = "Error generating feedback: " + e.getMessage();
        }

        return feedback;
    }
}


