package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LernmodusController {

    @FXML
    private TextField txtQuestion;
    @FXML
    private TextField txtCorrectAnswer;
    @FXML
    private TextField txtWrongAnswers;

    @FXML
    private void onSaveClick(ActionEvent event) {
        // Frage und Antworten auslesen
        String question = txtQuestion.getText();
        String correct = txtCorrectAnswer.getText();
        String wrongs = txtWrongAnswers.getText();

        // z.B. falsche Antworten per Komma trennen
        // oder anderes Format
        // Dann irgendwo speichern (Datei, DB, Cloud?)

        System.out.println("Speichere Frage: " + question);
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        // Zurück zum Hauptmenü
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Hier kannst du wie vorher main_menu.fxml laden
        // oder du rufst die MainMenuController-Methode
    }
}
