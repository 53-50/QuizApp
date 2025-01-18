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
                String result = "Question: " + answer.getQuestion() + "\n"
                        + "Your Answer: " + answer.getUserAnswer() + "\n"
                        + (answer.isCorrect() ? "Correct" : "Wrong! Correct Answer: " + answer.getCorrectAnswer())
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
