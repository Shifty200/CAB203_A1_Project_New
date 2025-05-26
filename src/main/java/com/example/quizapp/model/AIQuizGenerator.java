package com.example.quizapp.model;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIQuizGenerator {

    public static String generateQuiz(String prompt) {
        String fullResponse = "";

        try {
            URL url = new URL("http://localhost:11434/api/chat");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // formatting a specific question to the chat to get the structured response -- got info from llama api website for reference
            JSONObject formatSpec = new JSONObject()
                    .put("type", "object")
                    .put("properties", new JSONObject()
                            .put("questions", new JSONObject()
                                    .put("type", "array")
                                    .put("items", new JSONObject()
                                            .put("type", "object")
                                            .put("properties", new JSONObject()
                                                    .put("question", new JSONObject().put("type", "string"))
                                                    .put("options", new JSONObject().put("type", "array").put("items", new JSONObject().put("type", "string")))
                                                    .put("correctIndex", new JSONObject().put("type", "integer"))
                                            )
                                            .put("required", new org.json.JSONArray().put("question").put("options").put("correctIndex"))
                                    )
                            )
                    )
                    .put("required", new org.json.JSONArray().put("questions"));

            JSONObject requestJson = new JSONObject()
                    .put("model", "llama3.2:latest")
                    .put("messages", new org.json.JSONArray().put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt)))
                    .put("stream", false)
                    .put("format", formatSpec)
                    .put("options", new JSONObject().put("temperature", 0));

            // send the request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestJson.toString().getBytes());
            }

            // read the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    sb.append(responseLine);
                }
                JSONObject responseJson = new JSONObject(sb.toString());
                fullResponse = responseJson.getJSONObject("message").getString("content");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fullResponse;
    }

    // another prompt to create an ai gen title for the quiz
    public static String generateQuizTitle(String fullResponse) {
        String title = "";

        try {
            URL url = new URL("http://localhost:11434/api/chat");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = "Return ONLY a short, relevant quiz title (5–8 words) for this quiz content. No extra explanation, no punctuation, just the title:\n\n" + fullResponse;

            JSONObject requestJson = new JSONObject()
                    .put("model", "llama3.2:latest")
                    .put("messages", new org.json.JSONArray().put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt)))
                    .put("stream", false)
                    .put("options", new JSONObject().put("temperature", 0));

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
                title = responseJson.getJSONObject("message").getString("content").replaceAll("\"", "").trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return title;
    }

}
