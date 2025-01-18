package com.example.Controller;

import com.example.Questions.AnswerEvaluation;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class EvaluationController {
    @FXML
    private ListView<String> evaluationListView;

    private List<AnswerEvaluation> answers;

    public void setAnswers(List<AnswerEvaluation> answers) {
        this.answers = answers;
        displayEvaluation();
    }

    private void displayEvaluation() {
        if (answers != null) {
            for (AnswerEvaluation answer : answers) {
                String result = "Frage: " + answer.getQuestion() + "\n"
                        + "Deine Antwort: " + answer.getUserAnswer() + "\n"
                        + (answer.isCorrect() ? "Richtig" : "Falsch! Richtige Antwort: " + answer.getCorrectAnswer())
                        + "\n";
                evaluationListView.getItems().add(result);
            }
        }
    }

    @FXML
    private void onCloseClick() {
        evaluationListView.getScene().getWindow().hide();
    }
}
