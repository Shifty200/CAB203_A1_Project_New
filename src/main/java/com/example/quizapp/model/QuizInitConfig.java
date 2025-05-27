package com.example.quizapp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

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

    public static String readLinesFromFile(String filePath) throws IOException {
        try {
            File uploadedFile = new File(filePath);
            if (!filePath.endsWith(".txt")){
                throw new FileNotFoundException("Not a .txt file");
            }

            Scanner scan = new Scanner(uploadedFile);

            String fileContent = "";
            while (scan.hasNextLine()) {
                fileContent = fileContent.concat(scan.nextLine() + "\n");
            }
            return fileContent;

        } catch (Exception e) {
            QuizAppAlert fileAlert = new QuizAppAlert();
            fileAlert.alert("File Error", "There was an error with this file!",e.getMessage());

            return null;
        }

    }
}
