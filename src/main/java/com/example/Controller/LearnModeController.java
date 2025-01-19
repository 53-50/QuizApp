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

public class LearnModeController implements QuizBase {

    @FXML
    private Label lblQuestion; // Zeigt die aktuelle Frage an
    @FXML
    private TextField txtAnswer; // Eingabefeld für die Antwort des Benutzers
    @FXML
    private Button btnSubmit; // Button zum Absenden der Antwort
    @FXML
    private Button btnNext; // Button um zur nächsten Frage zu kommen
    @FXML
    private Button btnEvaluation; // Button um die Auswertung an zu zeigen
    @FXML
    private Label lblResult; // Zeigt an ob die Antwort richtig oder falsch war
    @FXML
    private Label timerLabel; // Label für die Anzeige des Timers

    private List<LernmodusQuestion> questions; // Liste der Fragen die aus der CSV-Datei geladen wurden
    private List<AnswerEvaluation> answers; // Liste mit Bewertungen der Antworten des Benutzers
    private int currentQuestionIndex; // Index der aktuell angezeigten Frage

    private Timeline timer; // Animations-Timer zur Aktualisierung der Zeit
    private int elapsedSeconds; // Zähler für die vergangenen Sekunden

    @Override
    public void setPlayerName(String name) {} // Interface aus QuizBase - Wird für Lernmodus nicht verwendet

    // Wird beim Laden des Controllers aufgerufen
    // Initialisiert die Listen questions und answers
    // Setzt den aktuellen Frageindex auf 0 damit bei der ersten Frage begonnen wird
    // Versteckt bzw. deaktiviert Buttons und das Ergebnis-Label zum Start
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

    // Methode zum starten des Timers
    @Override
    public void startTimer() {
        elapsedSeconds = 0; // Setzt den Timer-Zähler auf 0
        // Erstellt eine Timeline, die ein Ereignis (hier ein KeyFrame) in regelmäßigen Abständen ausführt
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> { // Führt jede Sekunde die Lambda-Funktion aus
            elapsedSeconds++; // Erhöht den Zähler für vergangene Sekunden
            updateTimerLabel(); // Aktualisiert den Timer-Label
        }));
        timer.setCycleCount(Timeline.INDEFINITE); // Lässt den Timer unbegrenzt laufen
        timer.play(); // Startet den Timer
    }

    // Methode zum zurücksetzen des Timers
    @Override
    public void resetTimer() {
        if (timer != null) { // Überprüft ob ein Timer existiert
            timer.stop(); // Stoppt den Timer falls er läuft
        }
        elapsedSeconds = 0; // Setzt den Zähler auf 0
        updateTimerLabel(); // Aktualisiert das Timer-Label auf "00:00:00"
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
            resetTimer();
            startTimer();
            displayQuestion();
        } catch (IOException e) {
            showAlert("Error", "The CSV-File could not be read.");
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
    private void onSubmitClick() {
        if (currentQuestionIndex < questions.size()) {
            String userAnswer = txtAnswer.getText().trim();
            String correctAnswer = questions.get(currentQuestionIndex).getAnswer();
            boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);

            if (isCorrect) {
                lblResult.setText("Correct!");
                lblResult.setStyle("-fx-text-fill: green;");
            } else {
                lblResult.setText("Wrong! The correct answer is: " + correctAnswer);
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
    private void onNextClick() {
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            lblQuestion.setText("All questions answered!");
            txtAnswer.setDisable(true);
            btnSubmit.setDisable(true);
            lblResult.setText("Quiz finished!");
            lblResult.setStyle("-fx-text-fill: black;");
            lblResult.setVisible(true);
            stopTimer();
            btnNext.setVisible(false);
            btnEvaluation.setVisible(true);
        }
    }

    @FXML
    private void onEvaluationClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/evaluation_layout.fxml"));
            Parent evaluationRoot = loader.load();

            EvaluationController evaluationController = loader.getController();
            evaluationController.setAnswers(answers);

            Stage stage = new Stage();
            stage.setTitle("Evaluation");
            stage.setScene(new Scene(evaluationRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        switchScene(event, "/Layouts/main_menu.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public List<LernmodusQuestion> getQuestions() {
        return questions;
    }

}
