package com.example.Controller;

import com.example.Interface.ControllerBase;
import com.example.Interface.QuizBase;
import com.example.Services.TriviaApiService;
import com.example.Questions.TriviaQuestion;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class QuizController implements QuizBase, ControllerBase {

    //TODO - Katrin - Highscore
    private int correctAnswerHigh = 0;//Variable to save correct Answers for Highscore-list

    // ~~~~~~ TIMER ~~~~~~
    private Timeline timer; //der Timer für onExit
    private int timeRemaining = 15; //Zeit des Timers

    // ~~~~~~ Rahmenbedingungen ~~~~~~
    private int punkte;
    private int questionCount = 1; // Tracks how many questions have been asked
    final private int questions = 10; // The total number of questions for the quiz
    private int leben = 5;
    private int rightOnes;


    //TODO
    private String playerName; //namen übergabe
    public void setPlayerName(String name) {
        this.playerName = name;
    } //Damit Spieler-Name übergeben wird

    // ~~~~~~ API ~~~~~~
    //TriviaQuestion Klasse erstellen um später dann aus der API einzelne Variablen rauszuholen
    TriviaQuestion recentQuestion;
    //Default Difficulty wird benötigt, damit API funktioniert -> auch wenn es dann geändert wird
    private String difficultyQC = "easy"; // Default difficulty

    @FXML
    public Label quizLivesLabel;
    @FXML
    public Label quizPointsLabel;
    @FXML
    public Label quizTimerLabel;

    @FXML
    private Label feedbackLabel;
    @FXML
    private Label progressLabel;
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


    public QuizController() throws IOException, InterruptedException {
        //leerer Konstruktor für API-FETCH - Wurde automatisch hinzugefügt -> Herausfinden wieso wichtig
    }

    //Startfunktion für TriviaQuiz

    @FXML
    public void initialize() throws IOException, InterruptedException {

        loadNewQuestion(true);

        // Timer initialisiert
        startTimer();

        //DEBUGGING
        //System.out.println("~DEBUGGING~ *ini* displayCurrentQuestion() aufgerufen, timeRemaining=" + timeRemaining);
        //System.out.println("~DEBUGGING~ *ini* handleTimeOut() aufgerufen, timeRemaining=" + timeRemaining);

        // Rahmenbedingungen setzen
        quizPointsLabel.setText(Integer.toString(punkte));
        quizLivesLabel.setText(Integer.toString(leben));
        progressLabel.setText(questionCount + "/" + questions);

    }
    private void startTimer() {
        if (timer != null) {
            timer.stop(); // Bestehenden Timer stoppen, falls aktiv
        }

        // Initiale Anzeige der Zeit
        quizTimerLabel.setText(timeRemaining + "s");

        // Timeline für den Countdown was pro Sekunde passiert
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

            //Debugging
            System.out.println("~DEBUGGING~ *ST* Timer tick: " + timeRemaining);

            timeRemaining--;
            quizTimerLabel.setText(timeRemaining + "s");

            if (timeRemaining <= 0) {
                timer.stop(); // Timer stoppen
                try {
                    //Debugging
                    System.out.println("~DEBUGGING~ *ST* Timeout! Going to handleTimeOut()");
                    handleTimeOut(); // Zeit ist abgelaufen
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE); // Timer läuft kontinuierlich
        timer.play(); // Timer starten
    }

    //was passiert wenn zeit vorbei

    private void handleTimeOut() throws IOException {

        timer.stop();
        System.out.println("~DEBUGGING~: *HTO* Timer gestoppt in handleTimeOut()"); //Debugging

        // Frage als falsch werten und zur nächsten wechseln
        markQuestionAsWrong();
        showFeedback("Time is up!", false);

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        answerBtn1.setDisable(true);
        answerBtn2.setDisable(true);
        answerBtn3.setDisable(true);
        answerBtn4.setDisable(true);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        // Zeige das Feedback für 3 Sekunden und lade dann die nächste Frage
        PauseTransition feedbackPause = new PauseTransition(Duration.seconds(3));
        feedbackPause.setOnFinished(event -> {
            try {
                loadNewQuestion(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        feedbackPause.play();

    }
    private void resetTimer() {
        // Timer stoppen, bevor wir ihn zurücksetzen
        // Timer wird nur einmal gestartet und nicht mehrfach,
        //  wodurch du verhindern kannst, dass Timer mehrere Instanzen hat oder unkontrolliert springt.
        // Durch das Stoppen des Timers vor dem Zurücksetzen stellen wir sicher,
        // dass der Countdown nach der Pause korrekt fortgesetzt wird und nicht sofort nach
        // der Pause springt.
        if (timer != null) {
            timer.stop();
        }

        // Zeit zurücksetzen und Timer erneut starten
        timeRemaining = 15;
        startTimer();
    }

    public void loadNewQuestion(boolean isFirstQuestion) throws IOException {

        if (!isFirstQuestion) {
            questionCount++; //Zähler für gestellte Fragen
        }

        System.out.println("~DEBUGGING~ *LNQ* loadNewQuestion aufgerufen. isFirstQuestion = " + isFirstQuestion);

        // Prüfe, ob das Quiz weitergeht
        if (leben > 0 && questionCount <= questions) {
            if (!isFirstQuestion) {
                // Verzögerung nur bei nachfolgenden Fragen
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    prepareForNextQuestion();
                });
                pause.play();
            } else {
                // Direkt neue Frage vorbereiten
                System.out.println("~DEBUGGING~ *LNQ* New Questione Else");
                prepareForNextQuestion();
            }
        } else {
            // Quiz beenden
            System.out.println("~DEBUGGING~ Quiz endet. Endscreen wird geladen.");
            showEndScreen();
        }
    }

        private void prepareForNextQuestion() {
            // Buttons und Feedback zurücksetzen
            answerBtn1.setDisable(false);
            answerBtn2.setDisable(false);
            answerBtn3.setDisable(false);
            answerBtn4.setDisable(false);
            feedbackLabel.setVisible(false);

            // Fortschrittsanzeige aktualisieren
            progressLabel.setText(questionCount + "/" + questions);

            // Farben und Timer zurücksetzen
            resetAnswerButtonColors();
            resetTimer();

            try {
                displayCurrentQuestion();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    public void checkAnswer(String givenAnswer) throws IOException, InterruptedException {

     if (recentQuestion.getCorrectAnswer().equals(givenAnswer)) {
            markQuestionAsRight();
            showFeedback("Correct", true);
        } else {
            markQuestionAsWrong();
            showFeedback("Incorrect", false);
        }

       /* DEBUGGING
        System.out.println("~DEBUGGING~ *cA* Correct Answer: " + TriviaApiService.fetchSingleQuestion(difficulty).getCorrectAnswer()); //Debugging
        System.out.println("~DEBUGGING~ *cA* Correct Answer: " + recentQuestion); //Debugging
        System.out.println("~DEBUGGING~ *cA* Given Answer: " + givenAnswer); //Debugging
        */

    }


    // anzeigen ob richtig oder falsch
    private void showFeedback(String feedback, boolean isCorrect) {
        feedbackLabel.setText(feedback);
        feedbackLabel.setStyle(isCorrect ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        feedbackLabel.setVisible(true);  // Feedback anzeigen
    }


    // damit die frage welche dran ist mit den dazugehörigen antworten angezeigt wird
    private void displayCurrentQuestion() throws IOException, InterruptedException {
        // nachgeschaut ob noch fragen da sind
        //DEBUGGING
        System.out.println("~DEBUGGING~ *DCQ* -> questionCount=" + questionCount
        + ", timeRemaining=" + timeRemaining);

        if (leben != 0) {

            recentQuestion = TriviaApiService.fetchSingleQuestion(difficultyQC); // Fetch a new question based on the selected difficulty

            // progressLabel.setText("~DEBUGGING~ *DCQ* Question " + questionCount + " of " + MAX_QUESTIONS); // Update the progress label
            System.out.println("~DEBUGGING~ *DCQ* Loaded Question: " + recentQuestion.getQuestionText()); // Debugging

            questionLabel.setText(recentQuestion.getAPIQuestionData().getText());

            // Combine and shuffle answers
            List<String> allAnswers = new ArrayList<>();
            allAnswers.add(recentQuestion.getCorrectAnswer());
            allAnswers.addAll(recentQuestion.getIncorrectAnswers());
            Collections.shuffle(allAnswers);

            // Assign answers to buttons
            answerBtn1.setText(allAnswers.get(0));
            answerBtn2.setText(allAnswers.get(1));
            answerBtn3.setText(allAnswers.get(2));
            answerBtn4.setText(allAnswers.get(3));

            // Store correct answer in button UserData
            answerBtn1.setUserData(allAnswers.get(0).equals(recentQuestion.getCorrectAnswer()));
            answerBtn2.setUserData(allAnswers.get(1).equals(recentQuestion.getCorrectAnswer()));
            answerBtn3.setUserData(allAnswers.get(2).equals(recentQuestion.getCorrectAnswer()));
            answerBtn4.setUserData(allAnswers.get(3).equals(recentQuestion.getCorrectAnswer()));

        } else {
            System.out.println("~DEBUGGING~ *DCQ* displayCurQue Endscreen");
            showEndScreen(); // Falls keine weiteren Fragen vorhanden sind zeig das Ende
        }
    }



    @FXML
    public void onAnswerClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        //TODO
        System.out.println("~DEBUGGING~ *oAC* Timer gestoppt bei Antwortauswahl");
        // Stoppe den Timer
        if (timer != null) {
            timer.stop();
        }

        Button clickedButton = (Button) actionEvent.getSource();
        String selectedAnswer = clickedButton.getText();

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        answerBtn1.setDisable(true);
        answerBtn2.setDisable(true);
        answerBtn3.setDisable(true);
        answerBtn4.setDisable(true);

        checkAnswer(selectedAnswer);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        // Zeige das Feedback für 2 Sekunden und lade dann die nächste Frage
        PauseTransition feedbackPause = new PauseTransition(Duration.seconds(1)); //Zeigt antwort für 1 Sekunde
        feedbackPause.setOnFinished(event -> {
            try {
                loadNewQuestion(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        feedbackPause.play();
    }

    // ~~~~~~~~~~~~~ WEITERE METHODEN ~~~~~~~~~~~~~

    public void onExit(ActionEvent event) {
        QuizBase.super.onExit(event, timer); // Ruft die default-Methode aus dem Interface auf
    }

    private void markQuestionAsWrong() {
        leben--;
        QuizBase.super.markQuestionAsWrong(getLeben(), quizLivesLabel);
    }

    private void markQuestionAsRight() {
        punkte += 10;
        rightOnes++;
        QuizBase.super.markQuestionAsRight(getPunkte(), getRightOnes(), quizPointsLabel);
    }

    private void showEndScreen() {
        // brauchen wir für show endscreen
        Stage stage = (Stage) questionLabel.getScene().getWindow();
        QuizBase.super.showEndScreen(stage, playerName, this); // Ruft die default-Methode aus dem Interface auf
    }


    // ~~~~~~~~~~~~~ GETTER & SETTER ~~~~~~~~~~~~~

    // vom interface damit der exit button da ist

    public void setDifficulty(String selectedDifficultyQC) { //Brauchen wir
        this.difficultyQC = selectedDifficultyQC; // Set the difficulty based on user selection
    }

    public void setAnswerButtonColors() {
        // Über alle Antwort-Buttons iterieren und die Farben ändern
        Button[] answerButtons = {answerBtn1, answerBtn2, answerBtn3, answerBtn4};

        for (Button button : answerButtons) {
            String answer = button.getText();

            if (answer.equalsIgnoreCase(recentQuestion.getCorrectAnswer())) {
                button.setStyle("-fx-text-fill: green;");  // Richtige Antwort grün
            } else {
                button.setStyle("-fx-text-fill: red;");  // Falsche Antwort rot
            }
        }
    }

    public void resetAnswerButtonColors() {
        QuizBase.super.resetAnswerButtonColors(answerBtn1, answerBtn2, answerBtn3, answerBtn4);
    }

    //Schwierigkeitsgrad abfragen
    public String getDifficultyQC() {
        return difficultyQC;
    }

    /*
    public int getQuizPoints() {
        return quizPoints;
    }

    public int getQuizLives() {
        return quizLives;
    }

    public int getCorrectAnswerHigh() { //What is this for?
        return correctAnswerHigh;
    }

     */

    // ~~~~~~~~~~~~~ GETTER & SETTER WIN/LOSE ~~~~~~~~~~~~~

    public int getPunkte() {
        return punkte;
    }

    public int getLeben() {
        return leben;
    }

    public int getRightOnes() {
        return rightOnes;
    }

    public int getQuestions() {
        return questions;
    }

}

