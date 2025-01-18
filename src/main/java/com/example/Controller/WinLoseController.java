package com.example.Controller;

import com.example.Interface.ControllerBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.example.Highscore.HighscoreManager;


import java.io.IOException;

public class WinLoseController {

    @FXML
    private Label modeLabel;
    @FXML
    private Label pointsLabel;
    @FXML
    private Label livesLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label rightOnesLabel;
    @FXML
    private Text storyTextLabel;

    // namen
    @FXML
    private Label nameLabel;
    private String playerName;

    // Infos von Controller holen damit man Statistik befüllen kann
    private ControllerBase controller;

    // damit der modus angezeigt werden kann
    private String mode;
    private String difficulty;

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    // controller setzten: tutorial oder quiz?
    public void setQuizController(ControllerBase controller) {
        this.controller = controller;

        if (controller instanceof QuizController) { // wenn es ein QuizController ist
            difficulty = ((QuizController) controller).getDifficultyQC(); //hol dir aktuelle difficulty
            if (difficulty == null || difficulty.isEmpty()) { // Fehler Handeling
                difficulty = "Error";
            }
            mode = "Quiz " + difficulty;  // setze die Variable mode
        } else if (controller instanceof TutorialController) { //wenn tutorial dann wird geschichte angezeigt
            storyTextLabel.setText("As we now know, she arrived a few years too early, " +
                            "missing the historic achievement of becoming the first cheese connoisseur in space. " +
                            "Tragically, her dream was cut short by human error. Let us take a moment to honor " +
                            "this unknown mouse, who gave her life in pursuit of her heroic aspirations. "+
                            "THE END ");
            storyTextLabel.setVisible(true);
            mode = "Tutorial"; // setze die Variable mode
        } else if (controller instanceof LernmodusController){ // wenn lernmodus
            mode = "Learning"; // setze die Variable mode
        }

    }


// ------------ DISPLAY LOGIC ------------
    public void displayMode() {
        modeLabel.setText("Mode: " + mode);
    }

    public void displayPoints() {
        int finalPoints = controller.getPoints();

        // wenn noch leben übrig sind => pro Lebel +5 Punkte
        if (controller.getLives() > 0) {
            finalPoints += controller.getLives() * 5;
        }
        pointsLabel.setText("Points: " + finalPoints);
    }

    public void displayRight() {
        int rightOnes = controller.getRightOnes();
        rightOnesLabel.setText("Right Answers: " + rightOnes + "/" + controller.getQuestions());
    }

    public void displayLives() {
        int finalLives = controller.getLives();

        if (finalLives < 0) {
            livesLabel.setText("Remaining Lives: None");
        } else {
            livesLabel.setText("Remaining Lives: " + finalLives);
        }
    }

    public void displayWinOrLose() {
        if (controller.getLives() > 0) {
            endLabel.setText("You Won! Congratulations little Mouse!");
        } else {
            endLabel.setText("You died like the mouse! :(");
        }
    }

    public void displayNameLabel() {
        nameLabel.setText(playerName);
    }


// ------------ BUTTON LOGIC (Retry, Home, Highscore) ------------

    // allgemeiner scene switch
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            //equals funktioniert mit String gut - muss man nicht overridden weil keine extra Logik benötigt
            if (fxmlPath.equals("/Layouts/tutorial_layout.fxml")) { // wenn es tutorial ist
                TutorialController tutorialController = loader.getController();
                tutorialController.setPlayerName(playerName); // Name wieder geben
            }

            if (fxmlPath.equals("/Layouts/quiz_layout.fxml")) { // wenn es quiz ist
                QuizController quizController = loader.getController();
                quizController.setPlayerName(playerName); // Name wieder geben
                quizController.setDifficulty(difficulty); // schwierigkeit wieder geben
            }

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    // spiel nochmal spielen
    public void onRetryClick(ActionEvent event) {
        if (controller instanceof TutorialController) {
            switchScene(event, "/Layouts/tutorial_layout.fxml");
        } else if (controller instanceof QuizController) {
            switchScene(event, "/Layouts/quiz_layout.fxml");
        } else if (controller instanceof LernmodusController) {
            switchScene(event, "/Layouts/lernmodus_layout.fxml");
        } else {
            switchScene(event, "/Layouts/main_menu.fxml");
        }
    }

    @FXML
    //Zum Hauptmenü
    public void onHomeClick(ActionEvent event) {
        switchScene(event, "/Layouts/main_menu.fxml");

        /*Name, Punkte und Schwierigkeit an Highscore Manager übergeben
        int finalePunkte = controller.getPunkte();
        String difficulty = "Leicht";
        HighscoreManager.getInstance().addScore(playerName, finalePunkte, difficulty);*/

        // Nur Werte vom Quiz Controller werden übernommen
        if (controller instanceof QuizController) {
            // Dann Highscore-Eintrag schreiben
            //TODO as soon as it works to give points to highscoreController - extra points if remaining lives calculated?
            int finalPoints = controller.getPoints();
            // Difficulty abfragen
            String difficulty = ((QuizController) controller).getDifficultyQC();


            HighscoreManager.getInstance().addScore(playerName, finalPoints, difficulty);
        }
    }

    // WinLoseLayoutController
    @FXML
    public void onHighscoreClick(ActionEvent event) {
        // Score ins Highscore-System übernehmen, nur wenn QuizController
        if (controller instanceof QuizController) {
            int finalPoints = controller.getPoints();
            String difficulty = ((QuizController) controller).getDifficultyQC();
            HighscoreManager.getInstance().addScore(playerName, finalPoints, difficulty);
        }

        // Wechsel in die Highscore-Szene
        switchScene(event, "/Layouts/highscore_layout.fxml");
    }

}
