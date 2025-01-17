package com.example.Controller;

import com.example.Interface.ControllerBase;
import com.example.Interface.QuizBase;
import com.example.Questions.QuestionLoader;
import com.example.Questions.TutorialQuestions;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.animation.PauseTransition;

public class TutorialController implements QuizBase, ControllerBase {

    @FXML
    private Label tutLivesLabel;

    @FXML
    private Label tutPointsLabel;

    @FXML
    private Label tutTimerLabel;

    @FXML
    private Button tutAnswer1;

    @FXML
    private Button tutAnswer2;

    @FXML
    private Button tutAnswer3;

    @FXML
    private Button tutAnswer4;

    @FXML
    private Label tutQuestionLabel;

    @FXML
    private Label tutQuestionNumberLabel;

    @FXML
    private Label tutFeedbackLabel;

    //der Timer
    private Timeline timer;
    // Zeit pro Frage in Sekunde
    private int timeRemaining = 15;

    // festlegen der Rahmenbedingungen
    private int points;
    private int lives = 3;
    final private int questions = 6;
    private int rightOnes = 0;
    private int questionsAsked = 1;

    //jsonfile reinladen
    private List<TutorialQuestions> questionsJson;
    private int currentQuestionIndex = 0; //brauchen wir damit die fragen nacheinander ausgelesen werden

    //popups
    private Queue<String> popupMessages;  // Warteschlange für Pop-up-Nachrichten

    //namen übergabe
    private String playerName;

    @Override
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    @Override
    public void initialize() {
        // Fragen aus der JSON-Datei laden
        final String jsonFilePath = "src/main/resources/questions/questions.json";
        loadQuestions(jsonFilePath);

        // Timer initialisieren
        startTimer();

        // Falls Fragen geladen wurden, die erste Frage anzeigen
        if (questionsJson != null && !questionsJson.isEmpty()) {
            displayCurrentQuestion();
        } else {
            System.out.println("No questions found!");
        }

        // Rahmenbedingungen setzen
        tutPointsLabel.setText(Integer.toString(points));
        tutLivesLabel.setText(Integer.toString(lives));
        tutQuestionNumberLabel.setText(Integer.toString(questionsAsked) + "/" + Integer.toString(questions));

        //Queue Liste erstellen für Tutorial Pop-Ups
        popupMessages = new LinkedList<>();

        // Pop-up-Nachrichten hinzufügen
        popupMessages.add("1/3 Tutorial\n\nOnce upon a time, there was a mouse who wanted nothing more " +
                "than to find the cheese of her dreams - in outer space. " +
                "With a rocket and unwavering courage, she set off " +
                "to make history.\n" +
                "\n" +
                "Are you ready to test your knowledge of this brave mouse?");
        popupMessages.add("2/3 Tutorial\n\nYou gain one point if you have impressed the mouse.\n\n" +
                "You lose a life if you disappoint the mouse.\n\n" + "It's up to you!");
        popupMessages.add("3/3 Tutorial\n\nBut watch out for the timer! You only have " + timeRemaining + " seconds.\n\n" +
                "If you take too long, you will disappoint the mouse.");

        // kurze Pause bevor die Pop-Ups gezeigt werden da sich das Spiel erst initialisiert
        // und das Pop-Up sonst hinter die Quiz-Scene verschwindet
        PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
        pause.setOnFinished(event -> {
            // Zeige das erste Pop-up
            showNextPopup();
        });
        pause.play();
    }

    //es wird in der Queue Liste nachgeschaut ob was da ist und der Befehl gegeben das anzuzeigen
    private void showNextPopup() {
        if (!popupMessages.isEmpty()) {
            if (timer != null) {
                timer.pause();  // Pausiert den Timer
            }
            // Nächste Nachricht aus der Queue holen und anzeigen
            String message = popupMessages.poll();
            showPopup(message);
        } else {
            // Alle Pop-Ups wurden angezeigt - Timer fortsetzen
            if (timer != null) {
                timer.play();  // Timer fortsetzen
            }
        }
    }

    // system dass popups allgemein angezeigt werden und hintereinander angezeigt werden
    private void showPopup(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/popups.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupMessage(message);  // Nachricht setzen

            Stage popupStage = new Stage();
            popupStage.setTitle("Tutorial Pop-up");
            popupStage.setScene(new Scene(root));

            // Modus von Stage festzulegen
            // Blockiert alle anderen Fenster der Anwendung
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Aussehen und Verhalten von Stage ändern
            // keine Titelleiste + nicht verschiebbar od. größen verändertnd
            popupStage.initStyle(StageStyle.UNDECORATED);


            if (timer != null) {
                timer.pause();
                // Pausiert den Timer - ist doppelt drinnen
                // noch ausprobieren welchen man weggeben kann
            }

            popupStage.show();

            // Wenn das Pop-up geschlossen wird, zeige das nächste!!
            popupStage.setOnHidden(event -> showNextPopup());

        } catch (Exception e) { //Fehlerhandeling
            e.printStackTrace();
        }
    }

    @Override
    public void startTimer() {
        // Initiale Anzeige der Zeit
        tutTimerLabel.setText(timeRemaining + "s");

        // Timeline für den Countdown was pro Sekunde passiert
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            tutTimerLabel.setText(timeRemaining + "s");

            if (timeRemaining <= 0) {
                timer.stop(); // Timer stoppen
                handleTimeOut(); // Zeit ist abgelaufen
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE); // Timer läuft kontinuierlich
        timer.play(); // Timer starten
    }

    @Override
    //was passiert wenn zeit vorbei
    public void handleTimeOut() {
        // Frage als falsch werten und zur nächsten wechseln
        markQuestionAsWrong();
        showFeedback("Time is up! The mouse dies because of you :(", false);

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        tutAnswer1.setDisable(true);
        tutAnswer2.setDisable(true);
        tutAnswer3.setDisable(true);
        tutAnswer4.setDisable(true);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        loadNewQuestion();
    }

    // die Fragen werden geladen dafür wird der QuestionLoader verwendet
    public void loadQuestions(String jsonFilePath) {
        questionsJson = QuestionLoader.loadQuestionsFromJson(jsonFilePath);
        if (questionsJson == null || questionsJson.isEmpty()) { //Fehlermeldung
            System.out.println("Error loading the questions.");
        }
    }

    @Override
    // damit die frage welche dran ist mit den dazugehörigen antworten angezeigt wird
    public void displayCurrentQuestion() {
        // nachgeschaut ob noch fragen da sind
        if (currentQuestionIndex < questionsJson.size()) {
            //aktuelle frage wird sich geholt
            TutorialQuestions currentQuestion = questionsJson.get(currentQuestionIndex);

            // Frage anzeigen
            tutQuestionLabel.setText(currentQuestion.getQuestionText());

            // Antwortmöglichkeiten wurden randomized in TutorialQuestions hier nur Buttons zugewiesen
            List<String> allAnswers = currentQuestion.getAllAnswers();
            tutAnswer1.setText(allAnswers.get(0));
            tutAnswer2.setText(allAnswers.get(1));
            tutAnswer3.setText(allAnswers.get(2));
            tutAnswer4.setText(allAnswers.get(3));
        } else {
            timer.pause();
            showEndScreen(); // Falls keine weiteren Fragen vorhanden sind zeig das Ende
        }
    }

    // Ablauf was passiert wenn ein Antwort-Button vom User ausgewählt wurde
    @FXML
    @Override
    public void handleAnswerButtonClick(ActionEvent mainEvent) {
        Button clickedButton = (Button) mainEvent.getSource(); //hol dir den Button
        String selectedAnswer = clickedButton.getText(); //hol dir den Text vom Button

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        tutAnswer1.setDisable(true);
        tutAnswer2.setDisable(true);
        tutAnswer3.setDisable(true);
        tutAnswer4.setDisable(true);

        // Überprüfen, ob die Antwort korrekt ist
        checkAnswer(selectedAnswer);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        // Frage als beantwortet markieren und zur nächsten Frage wechseln
        loadNewQuestion();
    }

    // anzeigen ob richtig oder falsch
    @Override
    public void showFeedback(String feedback, boolean isCorrect) {
        tutFeedbackLabel.setText(feedback);
        tutFeedbackLabel.setStyle(isCorrect ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        tutFeedbackLabel.setVisible(true);  // Feedback anzeigen
    }

    // kontrollieren ob die givenAnswer stimmt oder nicht
    @Override
    public void checkAnswer(String givenAnswer) {
        TutorialQuestions currentQuestion = questionsJson.get(currentQuestionIndex);
        if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(givenAnswer)) {
            markQuestionAsRight();
            showFeedback("That's right! You're taking off like a rocket :)", true);
        } else {
            markQuestionAsWrong();
            showFeedback("The mouse dies because of you :(", false);
        }
    }

    private void markQuestionAsWrong() {
        lives--;
        QuizBase.super.markQuestionAsWrong(getLives(), tutLivesLabel);
    }

    private void markQuestionAsRight() {
        points += 10;
        rightOnes++;
        QuizBase.super.markQuestionAsRight(getPoints(), getRightOnes(), tutPointsLabel);
    }

    public void loadNewQuestion() {
        // Logik zum Laden der nächsten Frage

        timer.pause();

        questionsAsked++; // Zähler für gestellte Fragen erhöhen

        if (lives > 0 && questionsAsked <= questionsJson.size()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5)); // 3 Sekunden Pause

            pause.setOnFinished(event -> {
                // Nach der Pause: neue Frage anzeigen und antwortbutton wieder ermöglichen
                tutAnswer1.setDisable(false);
                tutAnswer2.setDisable(false);
                tutAnswer3.setDisable(false);
                tutAnswer4.setDisable(false);
                //index erhöhen
                currentQuestionIndex++;
                //feedback wieder wegmachen
                tutFeedbackLabel.setVisible(false);
                //fragenanzahl aktualisieren
                tutQuestionNumberLabel.setText(Integer.toString(questionsAsked) + "/" + Integer.toString(questions));

                resetAnswerButtonColors(); //farben der buttons zurücksetzen
                resetTimer(); // Timer zurücksetzen
                displayCurrentQuestion(); // Nächste Frage anzeigen
            });

            pause.play();
        } else {
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                timer.pause();
                showEndScreen();
            });
            pause.play();
        }
    }

    // richtig und falsch buttons anzeigen
    @Override
    public void setAnswerButtonColors() {
        // Über alle Antwort-Buttons iterieren und die Farben ändern
        Button[] answerButtons = {tutAnswer1, tutAnswer2, tutAnswer3, tutAnswer4};

        for (Button button : answerButtons) {
            String answer = button.getText();

            TutorialQuestions currentQuestion = questionsJson.get(currentQuestionIndex);
            if (answer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
                button.setStyle("-fx-text-fill: green;");  // Richtige Antwort grün
            } else {
                button.setStyle("-fx-text-fill: red;");  // Falsche Antwort rot
            }
        }
    }

    private void resetAnswerButtonColors() {
        QuizBase.super.resetAnswerButtonColors(tutAnswer1, tutAnswer2, tutAnswer3, tutAnswer4);
    }

    @Override
    public void resetTimer() {
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

    //GETTER
    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public int getRightOnes() {
        return rightOnes;
    }

    @Override
    public int getQuestions() {
        return questions;
    }

    //endscreen anzeigen
    private void showEndScreen() {
        // brauchen wir für show endscreen
        Stage stage = (Stage) tutQuestionLabel.getScene().getWindow();
        QuizBase.super.showEndScreen(stage, playerName, this); // Ruft die default-Methode aus dem Interface auf
    }

    // BUTTON: SCHLIEßEN

    public void onTutorialExit(ActionEvent event) {
        QuizBase.super.onExit(event, timer); // Ruft die default-Methode aus dem Interface auf
    }

}
