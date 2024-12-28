package com.example;

// QuizApp.java ist Einstiegspunkt der Anwendung
// verwendet JavaFX um Benutzeroberfläche zu laden + anzuzeigen

// Importiert Application-Klasse, die als Einstiegspunkt für JavaFX-Anwendungen dient.
import javafx.application.Application;
// Importiert FXMLLoader-Klasse, die zum Laden von FXML-Dateien verwendet wird.
import javafx.fxml.FXMLLoader;
// Importiert Parent-Klasse, die als Basis für alle UI-Komponenten in JavaFX dient.
import javafx.scene.Parent;
// Importiert Scene-Klasse, die die gesamte Benutzeroberfläche darstellt.
import javafx.scene.Scene;
// Importiert Stage-Klasse, die das Hauptfenster der Anwendung darstellt.
import javafx.stage.Stage;

// Klasse QuizApp erbt von JavaFX Application-Klasse = erforderlich, um JavaFX-Anwendung zu erstellen
public class QuizApp extends Application {

    // start-Methode wird von JavaFX aufgerufen, wenn Anwendung gestartet wird
    // Diese Methode erhält Stage-Objekt, das das Hauptfenster der Anwendung darstellt
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Hauptmenü-FXML laden => definiert Layout und die Benutzeroberfläche des Menüs.
        Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("/main_menu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuRoot, 500, 800);
        //zuweisen des css files zu der scene
        mainMenuScene.getStylesheets().add("/css/application.css");

        // Breite der Stage fix festlegen am Beginn
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);

        // Zum Debuggen vielleicht hilfreich weil Infos angezeigt wegen Versionen
        // System.out.println(System.getProperties());

        primaryStage.setTitle("QuizApp");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    // launch => Methode startet JavaFX-Anwendung - ruft intern start-Methode auf
    public static void main(String[] args) {
        launch(args);
    }
}