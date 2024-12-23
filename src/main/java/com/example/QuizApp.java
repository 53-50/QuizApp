package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class QuizApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Hauptmenü-FXML laden
        Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("/main_menu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuRoot, 500, 800);

        primaryStage.setTitle("QuizApp");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}