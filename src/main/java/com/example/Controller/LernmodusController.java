package com.example.Controller;

import com.example.Interface.QuizBase;
import com.example.Questions.AnswerEvaluation;
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
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LernmodusController implements QuizBase {

    @FXML
    private Label lblQuestion;
    @FXML
    private TextField txtAnswer;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnEvaluation;
    @FXML
    private Label lblResult;
    @FXML
    private Label timerLabel;

    private List<LernmodusQuestion> questions;
    private List<AnswerEvaluation> answers;
    private int currentQuestionIndex;

    private Timeline timer;
    private int elapsedSeconds;

    @Override
    public void setPlayerName(String name) {

    }

    @FXML
    public void initialize() {
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        currentQuestionIndex = 0;
        lblResult.setVisible(false);
        btnNext.setDisable(true);
        btnNext.setVisible(false);
        btnEvaluation.setVisible(false);
    }

    @Override
    public void startTimer() {
        elapsedSeconds = 0;
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedSeconds++;
            updateTimerLabel();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @Override
    public void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        elapsedSeconds = 0;
        updateTimerLabel();
    }

    @Override
    public void handleTimeOut() throws IOException {

    }

    @Override
    public void displayCurrentQuestion() throws IOException, InterruptedException {

    }

    @Override
    public void handleAnswerButtonClick(ActionEvent mainEvent) throws IOException, InterruptedException {

    }

    @Override
    public void showFeedback(String feedback, boolean isCorrect) {

    }

    @Override
    public void checkAnswer(String givenAnswer) {

    }

    @Override
    public void setAnswerButtonColors() {

    }

    private void updateTimerLabel() {
        int hours = elapsedSeconds / 3600;
        int minutes = (elapsedSeconds % 3600) / 60;
        int seconds = elapsedSeconds % 60;
        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerLabel.setText(formattedTime);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
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
                resetTimer();
                startTimer();
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
            lblResult.setVisible(false);

            btnNext.setVisible(false);
            btnNext.setDisable(true);
        }
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        if (currentQuestionIndex < questions.size()) {
            String userAnswer = txtAnswer.getText().trim();
            String correctAnswer = questions.get(currentQuestionIndex).getAnswer();
            boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);

            if (isCorrect) {
                lblResult.setText("Richtig!");
                lblResult.setStyle("-fx-text-fill: green;");
            } else {
                lblResult.setText("Falsch! Die richtige Antwort ist: " + correctAnswer);
                lblResult.setStyle("-fx-text-fill: red;");
            }
            lblResult.setVisible(true);

            answers.add(new AnswerEvaluation(
                    questions.get(currentQuestionIndex).getQuestion(),
                    userAnswer,
                    isCorrect,
                    correctAnswer
            ));

            btnNext.setVisible(true);
            btnNext.setDisable(false);
        }
    }

    @FXML
    private void onNextClick(ActionEvent event) {
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            lblQuestion.setText("Alle Fragen beantwortet!");
            txtAnswer.setDisable(true);
            btnSubmit.setDisable(true);
            lblResult.setText("Quiz beendet!");
            lblResult.setStyle("-fx-text-fill: black;");
            lblResult.setVisible(true);
            stopTimer();
            btnNext.setVisible(false);
            btnEvaluation.setVisible(true);
        }
    }

    @FXML
    private void onEvaluationClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/evaluation_layout.fxml"));
            Parent evaluationRoot = loader.load();

            EvaluationController evaluationController = loader.getController();
            evaluationController.setAnswers(answers);

            Stage stage = new Stage();
            stage.setTitle("Auswertung");
            stage.setScene(new Scene(evaluationRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
