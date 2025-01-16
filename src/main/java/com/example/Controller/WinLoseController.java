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
    private Label modusLabel;

    @FXML
    private Label punkteLabel;

    @FXML
    private Label lebenLabel;

    @FXML
    private Label endeLabel;

    @FXML
    private Label rightOnesLabel;

    @FXML
    private Text geschichteLabel;

    // namen
    @FXML
    private Label namenLabel;

    private String playerName;

    // Infos von Controller holen damit man Statistik befüllen kann
    private ControllerBase controller;

    // damit der modus angezeigt werden kann
    private String modus;

    private String difficulty;

    // controller setzten: tutorial oder quiz?
    public void setQuizController(ControllerBase controller) {
        this.controller = controller;

        if (controller instanceof QuizController) {
            difficulty = ((QuizController) controller).getDifficultyQC();
            if (difficulty == null || difficulty.isEmpty()) {
                difficulty = "Error";
            }
            modus = "Quiz " + difficulty;
        } else if (controller instanceof TutorialController) { //wenn tutorial dann wird geschichte angezeigt und modus
            geschichteLabel.setText("As we now know, she arrived a few years too early, " +
                            "missing the historic achievement of becoming the first cheese connoisseur in space. " +
                            "Tragically, her dream was cut short by human error. Let us take a moment to honor " +
                            "this unknown mouse, who gave her life in pursuit of her heroic aspirations. "+
                            "THE END ");
            geschichteLabel.setVisible(true);
            modus = "Tutorial";
        } else if (controller instanceof LernmodusController){
            modus = "LearningModus";
        }

    }

    public void displayModus() {
        modusLabel.setText("Mode:" + modus);
    }

    public void displayPunkte() {
        int finalePunkte = controller.getPoints();

        if (controller.getLives() > 0) {
            finalePunkte += controller.getLives() * 5;
        }
        punkteLabel.setText("Points: " + finalePunkte);
        System.out.println("~DEBUGGING~ *DP-WLC* punkte: " + finalePunkte);
    }

    public void displayRight() {
        int rightOnes = controller.getRightOnes();
        rightOnesLabel.setText(rightOnes + "/" + controller.getQuestions());
    }

    public void displayLeben() {
        int finaleLeben = controller.getLives();

        if (finaleLeben < 0) {
            lebenLabel.setText("Remaining Lives: None");
        } else {
            lebenLabel.setText("Remaining Lives: " + finaleLeben);
        }
    }

    public void displayWinOrLose() {
        if (controller.getLives() > 0) {
            endeLabel.setText("You Won! Congratulations little Mouse!");
        } else {
            endeLabel.setText("You died like the mouse! :(");
        }
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void displayNamenLabel() {
        String name = playerName;
        namenLabel.setText("Name: " + playerName);
    }

    // allgemeiner scene switch
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            //equals funktioniert mit String gut - muss man nicht overridden weil keine extra Logik benötigt
            if (fxmlPath.equals("/Layouts/tutorial_layout.fxml")) {
                TutorialController tutorialController = loader.getController();
                tutorialController.setPlayerName(playerName); // Name weitergeben
            }

            if (fxmlPath.equals("/Layouts/quiz_layout.fxml")) {
                QuizController quizController = loader.getController();
                quizController.setPlayerName(playerName); // Name weitergeben
                quizController.setDifficulty(difficulty);
            }

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            System.out.println("~DEBUGGING~ something went wrong");
        }
    }

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
            int finalePunkte = controller.getPoints();
            // Difficulty abfragen
            String difficulty = ((QuizController) controller).getDifficultyQC();


            HighscoreManager.getInstance().addScore(playerName, finalePunkte, difficulty);
        }
    }

    // WinLoseLayoutController
    @FXML
    public void onHighscoreClick(ActionEvent event) {
        // Score ins Highscore-System übernehmen, nur wenn QuizController
        if (controller instanceof QuizController) {
            int finalePunkte = controller.getPoints();
            String difficulty = ((QuizController) controller).getDifficultyQC();
            HighscoreManager.getInstance().addScore(playerName, finalePunkte, difficulty);
        }

        // Wechsel in die Highscore-Szene
        switchScene(event, "/Layouts/highscore_layout.fxml");
    }

}
