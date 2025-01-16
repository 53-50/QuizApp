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

    public Label modusLabel;
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

    // controller setzten: tutorial oder quiz?
    public void setQuizController(ControllerBase controller) {
        this.controller = controller;

        if (controller instanceof QuizController) {
            String difficulty = ((QuizController) controller).getDifficulty();
            if (difficulty == null || difficulty.isEmpty()) {
                difficulty = "Unknown";
            }
            modus = "Quiz -> " + difficulty;
        } else if (controller instanceof TutorialController) { //wenn tutorial dann wird geschichte angezeigt und modus
            geschichteLabel.setText("Wie wir nun erfahren haben, kam sie ein paar Jahre zu früh," +
                    "verpasste den ersten Käse im All und fiel tragisch menschlichem Versagen zum Opfer." +
                    "Lasst uns einen Moment innehalten, um diese unbekannte Maus zu ehren, die sich für" +
                    "ihren Traum geopfert hat." +
                    "THE END");
            geschichteLabel.setVisible(true);
            modus = "Tutorial";
        } else if (controller instanceof LernmodusController){
            modus = "LearningModus";
        }

    }

    public void displayModus() {
        modusLabel.setText("Modus:" + modus);
    }

    public void displayPunkte() {
        int finalePunkte = controller.getPunkte();

        if (controller.getLeben() > 0) {
            finalePunkte += controller.getLeben() * 10;
        }
        punkteLabel.setText("Punkte: " + finalePunkte);
    }

    public void displayRight() {
        int rightOnes = controller.getRightOnes();
        rightOnesLabel.setText("Richtig beantwortet: " + rightOnes + "/" + controller.getQuestions());
    }

    public void displayLeben() {
        int finaleLeben = controller.getLeben();

        if (finaleLeben < 0) {
            lebenLabel.setText("Remaining Leben: Keine");
        } else {
            lebenLabel.setText("Remaining Leben: " + finaleLeben);
        }
    }

    public void displayWinOrLose() {
        if (controller.getLeben() > 0) {
            endeLabel.setText("Gewonnen");
        } else {
            endeLabel.setText("Verloren! Keine Leben mehr übrig :(");
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
            int finalePunkte = controller.getPunkte();
            // Difficulty abfragen
            String difficulty = ((QuizController) controller).getDifficulty();


            HighscoreManager.getInstance().addScore(playerName, finalePunkte, difficulty);
        }
    }

    // WinLoseLayoutController
    @FXML
    public void onHighscoreClick(ActionEvent event) {
        // Score ins Highscore-System übernehmen, nur wenn QuizController
        if (controller instanceof QuizController) {
            int finalePunkte = controller.getPunkte();
            String difficulty = ((QuizController) controller).getDifficulty();
            HighscoreManager.getInstance().addScore(playerName, finalePunkte, difficulty);
        }

        // Wechsel in die Highscore-Szene
        switchScene(event, "/Layouts/highscore_layout.fxml");
    }

}
