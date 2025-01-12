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

public class WinLoseLayoutController {

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

        //wenn tutorial dann wird geschichte angezeigt und modus
        if (controller instanceof TutorialController) {
            geschichteLabel.setText("Wie wir nun erfahren haben, kam sie ein paar Jahre zu früh," +
                    "verpasste den ersten Käse im All und fiel tragisch menschlichem Versagen zum Opfer." +
                    "Lasst uns einen Moment innehalten, um diese unbekannte Maus zu ehren, die sich für" +
                    "ihren Traum geopfert hat." +
                    "THE END");
            geschichteLabel.setVisible(true);
            modus = "Tutorial";
        } else if(controller instanceof QuizController) {
            modus = "Quiz";
        }

    }

    public void displayModus() {
        modusLabel.setText("Modus:" + modus);
    }

    public void displayPunkte() {
        int finalePunkte = controller.getPunkte();
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
        switchScene(event, "/Layouts/tutorial_layout.fxml");
    }

    //Zum Hauptmenü
    public void onHomeClick(ActionEvent event) {
        switchScene(event, "/Layouts/main_menu.fxml");


        //Name, Punkte und Schwierigkeit an Highscore Manager übergeben
        int finalePunkte = controller.getPunkte();
        String difficulty = "Leicht";
        HighscoreManager.getInstance().addScore(playerName, finalePunkte, difficulty);
    }
}
