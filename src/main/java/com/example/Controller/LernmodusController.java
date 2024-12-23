package com.example.Controller;


import com.example.Questions.LernmodusQuestion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

public class LernmodusController {

    @FXML
    private Label lblQuestion; // Label zur Anzeige der aktuellen Frage
    @FXML
    private TextField txtAnswer; // Textfeld zur Eingabe der Antwort
    @FXML

    private List<LernmodusQuestion> questions; // Liste der hochgeladenen Fragen
    private int currentQuestionIndex = 0; // Aktueller Index in der Fragenliste
    private String csvFilePath; // Pfad zur CSV-Datei


    // Methode, um den Dateipfad zu setzen (wird vom MainMenuController aufgerufen)
    public void setCsvFilePath(String filePath) {
        this.csvFilePath = filePath;
        loadQuestionsFromCsv();
    }

    // CSV-Datei laden
    private void loadQuestionsFromCsv() {
        try {
            questions = LernmodusQuestion.importFromCsv(csvFilePath);
            if (questions == null || questions.isEmpty()) {
                lblQuestion.setText("Keine Fragen in der CSV-Datei gefunden.");
            } else {
                showQuestion(questions.get(currentQuestionIndex));
            }
        } catch (IOException e) {
            lblQuestion.setText("Fehler beim Laden der CSV-Datei: " + e.getMessage());
        }
    }

    // Zeigt die aktuelle Frage an
    private void showQuestion(LernmodusQuestion question) {
        lblQuestion.setText(question.getQuestionText());
        txtAnswer.clear(); // Antwortfeld leeren
    }

    // Wenn Benutzer auf "Weiter" klickt, zur nächsten Frage wechseln
    @FXML
    private void onSubmitClick(ActionEvent event) {
        if (questions == null || questions.isEmpty()) {
            return;
        }

        String userAnswer = txtAnswer.getText();
        String correctAnswer = questions.get(currentQuestionIndex).getCorrectAnswer();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            lblQuestion.setText("Richtig!");
        } else {
            lblQuestion.setText("Falsch! Die richtige Antwort war: " + correctAnswer);
        }

        // Nächste Frage laden
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion(questions.get(currentQuestionIndex));
        } else {
            lblQuestion.setText("Alle Fragen beantwortet!");
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            // Lade die Hauptmenü-Szene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            Scene mainMenuScene = new Scene(mainMenuRoot);

            // Wechsle zur Hauptmenü-Szene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainMenuScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
