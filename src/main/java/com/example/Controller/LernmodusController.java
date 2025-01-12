package com.example.Controller;

import com.example.Questions.LernmodusQuestion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String delimiter = line.contains(";") ? ";" : ",";
                String[] parts = line.split(delimiter);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Fehler", "Das Hauptmenü konnte nicht geladen werden");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
