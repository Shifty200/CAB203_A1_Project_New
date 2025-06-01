package com.example.quizapp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * A class used to initialise quizzes with their uploaded file, difficulty and question range.
 */

public class QuizInitConfig {
    private File uploadedFile;
    private double difficulty;
    private String questionRange;

    /**
     * Constructs a new quiz initialisation instance.
     * @param uploadedFile The .txt file the quiz will be generated from.
     * @param difficulty The difficulty of the quiz as a double.
     * @param questionRange The number of questions in the quiz.
     */
    public QuizInitConfig(File uploadedFile, double difficulty, String questionRange) {
        this.uploadedFile = uploadedFile;
        this.difficulty = difficulty;
        this.questionRange = questionRange;
    }

    /**
     * Retrieves the uploaded file for this instance.
     * @return The uploaded file.
     */
    public File getUploadedFile() { return uploadedFile; }

    /**
     * Retrieves the difficulty for this instance.
     * @return The difficulty as a double.
     */
    public double getDifficulty() { return difficulty; }

    /**
     * Gets the total number of questions for this quiz.
     * @return An integer representing the total number of questions.
     */
    public String getQuestionRange() { return questionRange; }

    /**
     * Reads the lines from a file and parses it into a single string.
     * @param filePath The filepath of the uploaded file.
     * @return The contents of the file as a string.
     * @throws IOException If the file is not a text file or another error occurred.
     */
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
