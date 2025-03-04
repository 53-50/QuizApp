package com.example.Controller;

import com.example.Interface.QuizBase;
import com.example.Questions.AnswerEvaluation;
import com.example.Questions.LearnmodeQuestion;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
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

    private List<LearnmodeQuestion> questions; // Liste der Fragen die aus der CSV-Datei geladen wurden
    private List<AnswerEvaluation> answers; // Liste mit Bewertungen der Antworten des Benutzers
    private int currentQuestionIndex; // Index der aktuell angezeigten Frage

    private Timeline timer; // Animations-Timer zur Aktualisierung der Zeit
    private int elapsedSeconds; // Zähler für die vergangenen Sekunden

    // Interface aus QuizBase - Wird für Lernmodus nicht verwendet
    @Override
    public void setPlayerName(String name) {}

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
        // Listener für das Antwortfeld - überprüft ob sich Text ändert
        // wenn leer dann wird btnSubmit disabled
        txtAnswer.textProperty().addListener((observable, oldValue, newValue) -> {
            btnSubmit.setDisable(newValue.trim().isEmpty());
        });
        btnSubmit.setDisable(true);
    }

    // Methode zum starten des Timers
    @Override
    public void startTimer() {
        elapsedSeconds = 0; // Setzt den Timer-Zähler auf 0
        // Erstellt eine Timeline, die ein Ereignis (hier ein KeyFrame) in regelmäßigen Abständen ausführt
        // Führt jede Sekunde die Lambda-Funktion aus
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedSeconds++; // Erhöht den Zähler für vergangene Sekunden
            updateTimerLabel(); // Aktualisiert den Timer-Label
        }));
        timer.setCycleCount(Timeline.INDEFINITE); // Lässt den Timer unbegrenzt laufen
        timer.play(); // Startet den Timer
    }

    @Override
    public void resetTimer() {
        if (timer != null) { // Überprüft ob ein Timer existiert
            stopTimer(); // Stoppt den Timer falls er läuft
        }
        elapsedSeconds = 0; // Setzt den Zähler auf 0
        updateTimerLabel(); // Aktualisiert das Timer-Label auf "00:00:00"
    }

    // Interface aus QuizBase - Wird für Lernmodus nicht verwendet
    @Override
    public void handleTimeOut() throws IOException {

    }

    // Interface aus QuizBase - Wird für Lernmodus nicht verwendet
    @Override
    public void displayCurrentQuestion() throws IOException, InterruptedException {

    }

    // Interface aus QuizBase - Wird für Lernmodus nicht verwendet
    @Override
    public void handleAnswerButtonClick(ActionEvent mainEvent) throws IOException, InterruptedException {

    }

    // Interface aus QuizBase - Wird für Lernmodus nicht verwendet
    @Override
    public void showFeedback(String feedback, boolean isCorrect) {

    }

    // Interface aus QuizBase - Wird für Lernmodus nicht verwendet
    @Override
    public void checkAnswer(String givenAnswer) {

    }

    // Interface aus QuizBase - Wird für Lernmodus nicht verwendet
    @Override
    public void setAnswerButtonColors() {

    }

    // Methode zur Aktualisierung des Timers
    private void updateTimerLabel() {
        int hours = elapsedSeconds / 3600; // Berechnet die Stunden aus den vergangenen Sekunden
        int minutes = (elapsedSeconds % 3600) / 60; // Berechnet die Minuten aus den verbleibenden Sekunden
        int seconds = elapsedSeconds % 60; // Berechnet die verbleibenden Sekunden
        // Formatiert die Zeit als HH:MM:SS
        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerLabel.setText(formattedTime); // Aktualisiert das Timer-Label mit der formatierten Zeit
    }

    // Methode zum stoppen des Timers
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    // Methode zum laden der Fragen aus dem CSV File
    public void loadQuestionsFromCsv(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // Öffnet die CSV-Datei und liest sie zeilenweise
            String line;

            reader.readLine(); // Überspringt die erste Zeile (Kopfzeile der CSV-Datei

            while ((line = reader.readLine()) != null) {
                String delimiter = line.contains(";") ? ";" : ","; // Prüft (Ternary Operator) ob Trennzeichen ein ";" falls nein dann ","
                String[] parts = line.split(delimiter); // Teilt die Zeile anhand des Trennzeichens in Spalten auf
                if (parts.length == 2) {
                    questions.add(new LearnmodeQuestion(parts[0], parts[1])); // Fügt nur gültige Zeilen mit genau zwei Spalten hinzu
                }
            }
            resetTimer(); // Setzt Timer zurück
            startTimer(); // Startet Timer neu
            displayQuestion(); // Zeigt die erste Frage an
        } catch (IOException e) {
            showPopup("The CSV-File could not be read.", true);
        }
    }

    // Methode zum anzeigen der Fragen
    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) { // Überprüft ob noch unbeantwortete Fragen übrig sind
            lblQuestion.setText(questions.get(currentQuestionIndex).getQuestion()); // Zeigt die aktuelle Frage an
            txtAnswer.clear(); // Löscht den Inhalt des Eingabefelds
            lblResult.setVisible(false); // Versteckt das Ergebnis-Label

            // Deaktiviert den "Next Question" Button
            btnNext.setVisible(false);
            btnNext.setDisable(true);
        }
    }

    // Methode zur Prüfung der Antwort und anzeige des Ergebnisses
    @FXML
    private void onSubmitClick() {
        if (currentQuestionIndex < questions.size()) {
            String userAnswer = txtAnswer.getText().trim(); // Ruft die eingegebene Antwort aus txtAnswer ab - Trim entfernt Leerzeichen
            String correctAnswer = questions.get(currentQuestionIndex).getAnswer(); // Ruft passende Antwort zur Frage ab
            boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer); // Speichert das Ergebnis des Vergleichs - korrekt = true, falsch = false

            // IF statement umd heraus zu finden welches Ergebnis ausgegeben wird basierend auf isCorrect
            if (isCorrect) {
                lblResult.setText("Correct!");
                lblResult.setStyle("-fx-text-fill: #5cd686;");
            } else {
                lblResult.setText("Wrong! The correct answer is: " + correctAnswer);
                lblResult.setStyle("-fx-text-fill: #e85c6c;");
            }
            lblResult.setVisible(true); // Ergebnis wird angezeigt
            btnSubmit.setDisable(true);

            // Fügt eine neue Instanz der Klasse AnswerEvaluation der Liste answers hinzu und erstellt ein neues AnswerEvaluation Objekt
            // mit den unten aufgelisteten Informationen - Wird für auswertung gespeichert
            answers.add(new AnswerEvaluation(
                    questions.get(currentQuestionIndex).getQuestion(),
                    userAnswer,
                    isCorrect,
                    correctAnswer
            ));

            // Aktiviert wieder den Button "Next Question"
            btnNext.setVisible(true);
            btnNext.setDisable(false);
            txtAnswer.setDisable(true);
        }
    }

    // Methode um zur nächsten Frage zu kommen
    @FXML
    private void onNextClick() {
        currentQuestionIndex++; // Erhöht den Index um eins wenn aufgerufen

        // Überprüft ob es noch Fragen gibt, falls nicht wird der EndScreen angezeigt
        // der "Next Question" inkl. "Submit" Button werden deaktiviert und "Evaluation" wird aktiviert
        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
            txtAnswer.setDisable(false);
        } else {
            lblQuestion.setText("All questions answered!");
            btnSubmit.setDisable(true);
            lblResult.setText("Quiz finished!");
            lblResult.setStyle("-fx-text-fill: white;");
            lblResult.setVisible(true);
            stopTimer();
            btnNext.setVisible(false);
            btnEvaluation.setVisible(true);
            txtAnswer.setDisable(true);
            txtAnswer.clear();
        }
        // txtAnswer.setDisable(false);
    }

    // Methode zum Anzeigen der Auswertungsliste
    @FXML
    private void onEvaluationClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/evaluation_layout.fxml")); // Ladet das passende Layout
            Parent evaluationRoot = loader.load(); // Speichert den Wurzelknoten des geladenen Layouts

            // Eine Referenz auf den passenden Controller zum Layout
            // Ruft Controller Instanz ab die der geladenen FXML-Datei zugeordnet ist
            EvaluationController evaluationController = loader.getController();
            // Übergibt die Liste der Antworten an den EvaluationController für die Auswertung
            evaluationController.setAnswers(answers);

            // Berechnete Zeit übergeben
            String formattedTime = String.format("%02d:%02d:%02d", elapsedSeconds / 3600, (elapsedSeconds % 3600) / 60, elapsedSeconds % 60);
            evaluationController.setCompletionTime(formattedTime);

            Stage stage = new Stage(); // Erstellt ein neues Stage Objekt für das Auswertungs Fenster
            stage.setTitle("Evaluation"); // Setzt den Titel
            stage.setScene(new Scene(evaluationRoot)); // Erstellt eine neue Szene die das geladene FXML Layout verwendet
            stage.show(); // Zeigt das Fenster an
        } catch (IOException e) {
            e.printStackTrace(); // Gibt im Falle einer Ausnahem die vollständige Fehlermeldung in der Konsole aus
        }
    }

    // Methode um zurück zum Hauptmenü zu kommen
    @FXML
    private void onBackClick(ActionEvent event) {
        QuizBase.super.onExit(event, timer); // Ruft die default-Methode aus dem Interface auf
    }

    // Methode zur Anzeige einiger PopUp´s
    public void showPopup(String message, boolean showExitButton) {
        try {
            // FXML Objekt wird erstellt um das PopUp Layout zu laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/popups.fxml"));
            // Ladet FXML-Layout in ein Parent-Objekt die als Wurzel für eine Szene dient
            Parent popupRoot = loader.load();

            // Ruft PopUp Controller auf
            PopupController popupController = loader.getController();
            // Übergibt Nachricht an den Controller
            popupController.setPopupMessage(message);

            popupController.exitButton.setVisible(false);

            // Erstellt neue Stage für separates Fenster
            Stage popupStage = new Stage();
            // Erstellt neue Szene mit dem geladenen Layout als Wurzel
            popupStage.setScene(new Scene(popupRoot));
            // Setzt Titel
            popupStage.setTitle("Message");
            // Blockiert andere Fenster, solange das Popup aktiv ist
            popupStage.initModality(Modality.APPLICATION_MODAL);
            // Übergibt der PopUpController-Instanz die Stage Referenz
            // ermöglicht schließung der Stage indem User zb auf "Close" klickt
            popupController.setPopupStage(popupStage);
            // Wartet, bis der Benutzer das Popup schließt
            popupStage.showAndWait();
        } catch (IOException e) {
            // Gibt vollständige Fehlermeldung in der Konsole aus
            e.printStackTrace();
        }
    }

    // Methode zum holen der Fragen für die startLearnMode Methode
    public List<LearnmodeQuestion> getQuestions() {
        return questions;
    }
}
