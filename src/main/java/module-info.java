module com.example.quizapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.json;
    requires jbcrypt;


    opens com.example.quizapp to javafx.fxml;
    exports com.example.quizapp;
    exports com.example.quizapp.controller;
    opens com.example.quizapp.controller to javafx.fxml;
    exports com.example.quizapp.model;
    opens com.example.quizapp.model to javafx.fxml;
}