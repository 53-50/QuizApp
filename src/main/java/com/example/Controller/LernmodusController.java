package com.example.Controller;

import com.example.Questions.LernmodusQuestion;
import javafx.animation.Timeline;
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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.time.temporal.ChronoUnit;
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
    private Button btnNext;
    @FXML
    private Label lblResult;
    @FXML
    private Label timerLabel;

    private List<LernmodusQuestion> questions;
    private int currentQuestionIndex;

    private Timeline timer;
    private LocalTime startTime;

    @FXML
    public void initialize() {
        questions = new ArrayList<>();
        currentQuestionIndex = 0;
        lblResult.setVisible(false);
        btnNext.setDisable(true);
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateTimer() {
        LocalTime now = LocalTime.now();
        long secondsElapsed = ChronoUnit.SECONDS.between(startTime, now);

        long hours = secondsElapsed / 3600;
        long minutes = (secondsElapsed % 3600) / 60;
        long seconds = secondsElapsed % 60;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerLabel.setText(formattedTime);
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
                startTimer();
                displayQuestion();
            } else {
                showAlert("Fehler", "Die CSV-Datei enthät keine gültigen Daten.");
            }
        } catch (IOException e) {
            showAlert("Fehler", "Die CSV-Datei konnte nicht gelesen werden");
        }
    }

    private void startTimer() {
        startTime = LocalTime.now();
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            lblQuestion.setText(questions.get(currentQuestionIndex).getQuestion());
            txtAnswer.clear();
            lblResult.setVisible(false);
            btnNext.setVisible(false);
            btnNext.setDisable(true);
        } else {
            lblQuestion.setText("Alle Fragen beantwortet!");
            txtAnswer.setDisable(true);
            btnSubmit.setDisable(true);
            lblResult.setText("Quiz beendet!");
            lblResult.setStyle("-fx-text-fill: black;");
            lblResult.setVisible(true);
            stopTimer();
        }
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        if (currentQuestionIndex < questions.size()) {
            String userAnswer = txtAnswer.getText().trim();
            String correctAnswer = questions.get(currentQuestionIndex).getAnswer();

            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                lblResult.setText("Richtig!");
                lblResult.setStyle("-fx-text-fill: green;");
            } else {
                lblResult.setText("Falsch! Die richtige Antwort ist: " + correctAnswer);
                lblResult.setStyle("-fx-text-fill: red;");
            }
            lblResult.setVisible(true);
            btnNext.setVisible(true);
            btnNext.setDisable(false);
        }
    }

    @FXML
    private void onNextClick(ActionEvent event) {
        currentQuestionIndex++;
        displayQuestion();
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.show();
            stopTimer();
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
