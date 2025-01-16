package com.example.Controller;

import com.example.Interface.ControllerBase;
import com.example.Interface.QuizBase;
import com.example.Questions.TutorialQuestions;
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

    private int questionCount = 0; // Tracks how many questions have been asked
    private static final int MAX_QUESTIONS = 10; // The total number of questions for the quiz
    private String difficulty = "easy"; // Default difficulty
    private int correctAnswerHigh = 0;//Variable to save correct Answers for Highscore-list
    private int quizLives = 2;
    private int quizPoints;

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

    //der Timer für onExit
    private Timeline timer;

    private int timeRemaining = 15;

    //namen übergabe
    private String playerName;

    TriviaQuestion recentQuestion;

    public QuizController() throws IOException, InterruptedException { //leerer Konstruktor für API-FETCH
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    } //Damit Name übergeben wird

    //TODO
    public void setDifficulty(String userdifficulty) { //Brauchen wir - TODO
        this.difficulty = userdifficulty; // Set the difficulty based on user selection
    }

    //Startfunktion für TriviaQuiz
    @FXML
    public void initialize() throws IOException, InterruptedException {

        displayCurrentQuestion();

        // Timer initialisiert
        startTimer();

        //DEBUGGING
        System.out.println("DEBUGGING displayCurrentQuestion() aufgerufen, timeRemaining=" + timeRemaining);
        System.out.println("DEBUGGING handleTimeOut() aufgerufen, timeRemaining=" + timeRemaining);

        // Rahmenbedingungen setzen
        quizPointsLabel.setText(Integer.toString(quizPoints));
        quizLivesLabel.setText(Integer.toString(quizLives));
        progressLabel.setText(questionCount + "/" + MAX_QUESTIONS);

    }

    public void loadNewQuestion() throws IOException {

        questionCount++; //Increment question count

        if (questionCount >= MAX_QUESTIONS) {
            progressLabel.setText("Quiz complete!");
            showEndScreen();
        } else {
           PauseTransition pause = new PauseTransition(Duration.seconds(3));

            pause.setOnFinished(event -> {
                // Nach der Pause: neue Frage anzeigen und antwortbutton wieder ermöglichen
                answerBtn1.setDisable(false);
                answerBtn2.setDisable(false);
                answerBtn3.setDisable(false);
                answerBtn4.setDisable(false);
                //feedback wieder wegmachen
                feedbackLabel.setVisible(false);
                //fragenanzahl aktualisieren
                progressLabel.setText(questionCount + "/" + MAX_QUESTIONS);

                resetAnswerButtonColors(); //farben der buttons zurücksetzen
                resetTimer(); // Timer zurücksetzen
                try {
                    displayCurrentQuestion(); // Nächste Frage anzeigen
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });

            pause.play();
        }
    }

    private void markQuestionAsWrong() {
        quizLives--;
        QuizBase.super.markQuestionAsWrong(getQuizLives(), quizLivesLabel);
    }

    private void markQuestionAsRight() {
        quizPoints++;
        correctAnswerHigh++;
        QuizBase.super.markQuestionAsRight(getQuizPoints(), getCorrectAnswerHigh(), quizPointsLabel);
    }


    private void startTimer() {
        // Initiale Anzeige der Zeit
        quizTimerLabel.setText(timeRemaining + "s");

        // Timeline für den Countdown was pro Sekunde passiert
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

            //Debugging
            System.out.println("Timer tick: " + timeRemaining);

            timeRemaining--;
            quizTimerLabel.setText(timeRemaining + "s");

            if (timeRemaining == 0) {
                timer.stop(); // Timer stoppen
                try {
                    //Debugging
                    System.out.println(" DEBUGGING Timeout! Going to handleTimeOut()");
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

        loadNewQuestion();
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
        System.out.println("Correct Answer: " + TriviaApiService.fetchSingleQuestion(difficulty).getCorrectAnswer()); //Debugging
        System.out.println("Correct Answer: " + recentQuestion); //Debugging
        System.out.println("Given Answer: " + givenAnswer); //Debugging
        */

    }

    // anzeigen ob richtig oder falsch
    private void showFeedback(String feedback, boolean isCorrect) {
        feedbackLabel.setText(feedback);
        feedbackLabel.setStyle(isCorrect ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        feedbackLabel.setVisible(true);  // Feedback anzeigen
    }


    //Schwierigkeitsgrad abfragen
    public String getDifficulty() {
        return difficulty;
    }

    public int getQuizPoints() {
        return quizPoints;
    }

    public int getQuizLives() {
        return quizLives;
    }

    public int getCorrectAnswerHigh() { //What is this for?
        return correctAnswerHigh;
    }

    // damit die frage welche dran ist mit den dazugehörigen antworten angezeigt wird
    private void displayCurrentQuestion() throws IOException, InterruptedException {
        // nachgeschaut ob noch fragen da sind
        //DEBUGGING
        System.out.println(" DEBUGGING displayCurrentQuestion() -> questionCount=" + questionCount
                + ", timeRemaining=" + timeRemaining);
        if (quizLives != 0) {

            recentQuestion = TriviaApiService.fetchSingleQuestion(difficulty); // Fetch a new question based on the selected difficulty

            // progressLabel.setText("Question " + questionCount + " of " + MAX_QUESTIONS); // Update the progress label
            System.out.println("Loaded Question: " + recentQuestion.getQuestionText()); // Debugging

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
            showEndScreen(); // Falls keine weiteren Fragen vorhanden sind zeig das Ende
        }
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

    // vom interface damit der exit button da ist
    public void onExit(ActionEvent event) {
        QuizBase.super.onExit(event, timer); // Ruft die default-Methode aus dem Interface auf
    }


    public void setAnswerButtonColors() {
        // logik für richtige button
    }

    public void resetAnswerButtonColors() {
        QuizBase.super.resetAnswerButtonColors(answerBtn1, answerBtn2, answerBtn3, answerBtn4);
    }

    private void showEndScreen() {
        // brauchen wir für show endscreen
        Stage stage = (Stage) questionLabel.getScene().getWindow();
        QuizBase.super.showEndScreen(stage, playerName, this); // Ruft die default-Methode aus dem Interface auf
    }


    @FXML
    public void onAnswerClick(ActionEvent actionEvent) throws IOException, InterruptedException {
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

        // Frage als beantwortet markieren und zur nächsten Frage wechseln
        loadNewQuestion();



        //boolean isCorrect = (boolean) clickedButton.getUserData();

        /*if (isCorrect) {
            feedbackLabel.setText("Correct! Well done.");
            feedbackLabel.setStyle("-fx-text-fill: green;"); // Set feedback text color to green
            correctAnswer++;
        } else {
            feedbackLabel.setText("Wrong! Try the next one.");
            feedbackLabel.setStyle("-fx-text-fill: red;"); // Set feedback text color to red
        }

        // Use Timeline to delay loading the next question
        Timeline timeline = new Timeline(new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(0.5), // Delay for 0.5 s
                event -> {
                    try {
                        // loadNewQuestion(); // Load the next question
                        displayCurrentQuestion();
                        feedbackLabel.setText(""); // Clear the feedback label
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        ));
        timeline.setCycleCount(1); // Execute the KeyFrame only once
        timeline.play(); // Start the timeline
        */
    }

    /*


    public void resetQuiz() {
        questionCount = 0;
        progressLabel.setText("Question 1 of " + MAX_QUESTIONS); // Reset the label
    }


     */
}

