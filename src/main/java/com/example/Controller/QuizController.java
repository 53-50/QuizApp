package com.example.Controller;

import com.example.TriviaApiService;
import com.example.TriviaQuestion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizController {

    private String difficulty = "easy"; // default, falls nichts gesetzt wird

    @FXML
    private Label questionLabel;
    @FXML
    private Button answerBtn1;
    @FXML
    private Button answerBtn2;
    @FXML
    private Button answerBtn3;
    @FXML
    private Button answerBtn4;

    // vom MainMenuController aufgerufen, bevor die Szene gezeigt wird
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @FXML
    public void initialize() {
        // Hier könntest du loadNewQuestion() aufrufen
        // oder du wartest, bis setDifficulty() aufgerufen ist.
        // Je nach Reihenfolge ist es manchmal besser, loadNewQuestion()
        // in einer @FXML-Init-Methode aufzurufen, die
        // evtl. durch ein kleines Delay verzögert wird.

        loadNewQuestion();
    }

    private void loadNewQuestion() {
        try {
            // z.B. so:
            // "https://the-trivia-api.com/v2/questions?limit=1&difficulty=<difficulty>"
            // Wichtig: Achte darauf, ob die Trivia-API "easy, medium, hard" exakt so akzeptiert
            // und ob du sonst ggf. "difficulties=easy" schreiben musst
            String url = "https://the-trivia-api.com/v2/questions?limit=1&difficulty=" + difficulty;
            TriviaQuestion question = TriviaApiService.fetchSingleQuestion(url);

            questionLabel.setText(question.getQuestion().getText());
            // etc. ...
            // Shuffle der Antworten, Buttons beschriften usw.

        } catch (IOException | InterruptedException e) {
            questionLabel.setText("Fehler: " + e.getMessage());
        }
    }
}
