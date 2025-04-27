package com.example.addressbook.model;

import java.io.File;

public class QuizInitConfig {
    private File uploadedFile;
    private double difficulty;
    private String questionRange;

    public QuizInitConfig(File uploadedFile, double difficulty, String questionRange) {
        this.uploadedFile = uploadedFile;
        this.difficulty = difficulty;
        this.questionRange = questionRange;
    }

    public File getUploadedFile() { return uploadedFile; }
    public double getDifficulty() { return difficulty; }
    public String getQuestionRange() { return questionRange; }
}
