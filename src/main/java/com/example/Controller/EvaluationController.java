package com.example.Controller;

import com.example.Questions.AnswerEvaluation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class EvaluationController {
    @FXML
    private ListView<String> evaluationListView; // Liste für die Anzeige innerhalb des Evaluation Layouts
    @FXML
    private Label timeLabel;

    private List<AnswerEvaluation> answers; // Liste für die Antworten

    // Setter für die Antworten
    public void setAnswers(List<AnswerEvaluation> answers) {
        this.answers = answers;
        displayEvaluation();
    }

    //Methode um die benötigte Zeit zu setzen
    public void setCompletionTime(String time) {
        if (timeLabel != null) {
            timeLabel.setText("Time taken: " + time);
            timeLabel.setStyle("-fx-text-fill: white;");
        }
    }

    // Methode für die Anzeige der gespeicherten Fragen, UserAntworten, Ergebnis und richtige Antwort falls Frage
    // falsch beantwortet wurde
    private void displayEvaluation() {
        if (answers != null) {
            // Timer hinzufügen
            for (AnswerEvaluation answer : answers) {
                // Erstellt einen String für jede beantwortete Frage
                String result = "Question: " + answer.getQuestion() + "\n"
                        + "Your Answer: " + answer.getUserAnswer() + "\n"
                        + (answer.isCorrect() ? "Correct" : "Wrong! Correct Answer: " + answer.getCorrectAnswer())
                        + "\n";
                // Fügt den String zur Liste hinzu
                evaluationListView.getItems().add(result);
            }
        }
    }

    // Methode zum schließen der Auswertung
    @FXML
    private void onCloseClick() {
        evaluationListView.getScene().getWindow().hide();
    }
}
