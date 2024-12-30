package com.example.Controller;

import com.example.Questions.LernmodusQuestion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LernmodusController {

    @FXML
    private Label lblQuestion;
    @FXML
    private TextField txtAnswer;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnBack;

    private List<LernmodusQuestion> questions;
    private int currentQuestionIndex;

    @FXML
    public void initialize() {
        questions = new ArrayList<>();
        currentQuestionIndex = 0;
    }

    public void loadQuestionsFromCsv(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    questions.add(new LernmodusQuestion(parts[0], parts[1]));
                }
            }
            if (!questions.isEmpty()) {
                displayQuestion();
            } else {
                showAlert("Fehler", "Die CSV-Datei enthät keine gültigen Daten.");
            }
        } catch (IOException e) {
            showAlert("Fehler", "Die CSV-Datei konnte nicht gelesen werden");
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            lblQuestion.setText(questions.get(currentQuestionIndex).getQuestion());
            txtAnswer.clear();
        } else {
            lblQuestion.setText("Alle Fragen beantwortet!");
            txtAnswer.setDisable(true);
            btnSubmit.setDisable(true);
        }
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        if (currentQuestionIndex < questions.size()) {
            String userAnswer = txtAnswer.getText().trim();
            String correctAnswer = questions.get(currentQuestionIndex).getAnswer();

            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                showAlert("Richtig!", "Deine Antwort ist korrrekt.");
            } else {
                showAlert("Falsch!", "Die richtige Antwort lautet: " + correctAnswer);
            }

            currentQuestionIndex++;
            displayQuestion();
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        System.out.println("Zurück zum Menü");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
