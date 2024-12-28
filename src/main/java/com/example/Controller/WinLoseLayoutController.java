package com.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class WinLoseLayoutController {

    @FXML
    private Label punkteLabel;

    @FXML
    private Label lebenLabel;

    @FXML
    private Label endeLabel;

    @FXML
    private Label rightOnesLabel;

    // namen
    @FXML
    private Label namenLabel;

    private String playerName;

    // Infos von TutorialController holen damit man Statistik befüllen kann
    private TutorialController tutorialController;

    public void setQuizController(TutorialController controller) {
        this.tutorialController = controller;
    }

    public void displayPunkte() {
        int finalePunkte = tutorialController.getPunkte();
        punkteLabel.setText("Punkte: " + finalePunkte);
    }

    public void displayRight() {
        int rightOnes = tutorialController.getRightOnes();
        rightOnesLabel.setText("Richtig beantwortet: " + rightOnes + "/" + tutorialController.getQuestions());
    }

    public void displayLeben() {
        int finaleLeben = tutorialController.getLeben();

        if (finaleLeben < 0) {
            lebenLabel.setText("Remaining Leben: Keine");
        } else {
            lebenLabel.setText("Remaining Leben: " + finaleLeben);
        }
    }

    public void displayWinOrLose() {
        if (tutorialController.getLeben() > 0) {
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

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // spiel nochmal spielen (TODO: Name wird bei Retry noch nicht mitübernommen)
    public void onRetryClick(ActionEvent event) {
        switchScene(event, "/tutorial_layout.fxml");
    }

    //Zum Hauptmenü
    public void onHomeClick(ActionEvent event) {
        switchScene(event, "/main_menu.fxml");
    }
}
