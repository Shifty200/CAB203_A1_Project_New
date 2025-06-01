package com.example.quizapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class used for generating quizzes from the AI response
 */
public class QuizAppUtil {
    // reverse the Array.toString method for an array of ints
    // from https://stackoverflow.com/questions/456367/reverse-parse-the-output-of-arrays-tostringint
    /**
     * reverse the Array.toString method for an array of ints
     * @param string The input string
     */
    public static int[] fromString(String string) {
        String[] strings = string.replace("[", "").replace("]", "").split(", ");
        int[] result = new int[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }
        return result;
    }

    /**
     * gets the AI response and turns it into questions
     * @param response The JSON response from the AI
     * @param quizName The name of the quiz
     * @param topic The topic name of the quiz
     * @param difficulty The difficulty of the quiz
     * @return The quiz object with the generated questions.
     */
    public static Quiz parseAIResponse(String response, String quizName, String topic, String difficulty) {
        Quiz quiz = new Quiz(quizName, topic, difficulty);

        JSONObject json = new JSONObject(response);
        JSONArray questionsArray = json.getJSONArray("questions");

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject q = questionsArray.getJSONObject(i);
            String questionText = q.getString("question");

            JSONArray optionsArray = q.getJSONArray("options");
            ArrayList<String> options = new ArrayList<>();
            for (int j = 0; j < optionsArray.length(); j++) {
                options.add(optionsArray.getString(j));
            }
            int correctIndex = q.getInt("correctIndex");
            quiz.addQuestion(new QuizQuestion(questionText, options, correctIndex));
        }

        return quiz;
    }
}

