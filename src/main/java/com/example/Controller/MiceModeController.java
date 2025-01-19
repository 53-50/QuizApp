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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.animation.PauseTransition;

public class MiceModeController implements QuizBase, ControllerBase {

    @FXML
    private Label miceLivesLabel;
    @FXML
    private Label micePointsLabel;
    @FXML
    private Label miceTimerLabel;
    @FXML
    private Button miceAnswer1;
    @FXML
    private Button miceAnswer2;
    @FXML
    private Button miceAnswer3;
    @FXML
    private Button miceAnswer4;
    @FXML
    private Label miceQuestionLabel;
    @FXML
    private Label miceQuestionNumberLabel;
    @FXML
    private Label miceFeedbackLabel;

    //der Timer
    private Timeline timer;
    // Zeit pro Frage in Sekunde
    private final int timeSet = 15;
    private int timeRemaining = timeSet;

    // festlegen der Rahmenbedingungen
    private int points;
    private int lives = 3;
    final private int questions = 11;
    private int rightOnes = 0;
    private int questionsAsked = 1;

    //jsonfile reinladen
    private List<TutorialQuestions> questionsJson; // hier werden alle Fragen rein gespeichert
    private int currentQuestionIndex = 0; //brauchen wir damit die fragen nacheinander ausgelesen werden
    // der index ist die stelle in der list

    //popups
    private Queue<String> popupMessages;  // Warteschlange für Pop-ups Nachrichten

    @Override
    public void setPlayerName(String name) {
        name = "Mr. Squeak";
    }


    @Override
    public void initialize() {
        // Fragen aus der JSON-Datei laden
        final String jsonFilePath = "src/main/resources/questions/mice.json";
        loadQuestions(jsonFilePath);

        // Timer initialisieren
        startTimer();

        // Falls Fragen geladen wurden, die erste Frage anzeigen
        if (questionsJson != null && !questionsJson.isEmpty()) {
            displayCurrentQuestion();
        } else {
            System.out.println("No questions found!");
        }

        // Rahmenbedingungen setzen in den Labels damit die Punkte, Leben und Fragen Anzahl angezeigt wird
        micePointsLabel.setText(Integer.toString(points));
        miceLivesLabel.setText(Integer.toString(lives));
        miceQuestionNumberLabel.setText("squeak/squeak");

        //Queue Liste erstellen für Pop-Ups
        popupMessages = new LinkedList<>();

        // Pop-up-Nachrichten hinzufügen
        popupMessages.add("squeak squeak squeak, squeak squeak squeak. piep, piep piep piep." +
                "i-i-i-i i-i-i-I, i-i-i-i i-i-i-i i-i-i-i. cleek cleek cleek, cleek cleek cleek." +
                "cin cin, cin cin cin. squitt-squitt, squitt-squitt squitt-squitt squitt-squitt." +
                "chuu-chuu chuu-chuu, chuu-chuu chuu-chuu chuu-chuu. jjik-jjik jjik-jjik jjik-jjik," +
                "jjik-jjik jjik-jjik jjik-jjik. zī-zī zī-zī zī-zī, zī-zī zī-zī zī-zī. cui-cui cui-cui," +
                "cui-cui cui-cui cui-cui. pip-pip, pip-pip pip-pip pip-pip.");

        // kurze Pause bevor die Pop-Ups gezeigt werden da sich das Spiel erst initialisiert
        // und das Pop-Up sonst hinter die Quiz-Scene verschwindet
        PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
        pause.setOnFinished(event -> {
            // Zeige das erste Pop-up
            showNextPopup();
        });
        pause.play();
    }


    // ------------ POP-UPS ------------
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
            // FXML wird geladen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/popups.fxml"));
            Parent root = loader.load();

            // loader.getController() = erhälts controller instance => erstellt + initialisiert von FXMLLoader
            PopupController popupController = loader.getController();
            popupController.setPopupMessage(message);  // Nachricht setzen

            // Aktuelle Stage holen und neue Szene setzen
            Stage popupStage = new Stage();
            popupStage.setTitle("squeak squeak squeak");
            popupStage.setScene(new Scene(root));

            // Modus von Stage festzulegen
            // Blockiert alle anderen Fenster der Anwendung
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Aussehen und Verhalten von Stage ändern
            // keine Titelleiste + nicht verschiebbar od. größen verändertnd
            popupStage.initStyle(StageStyle.UNDECORATED);

            popupStage.show();

            // Wenn das Pop-up geschlossen wird, zeige das nächste!!
            popupStage.setOnHidden(event -> showNextPopup());

        } catch (Exception e) { //Fehlerhandeling
            e.printStackTrace();
        }
    }


    // ------------ TIMER ------------
    @Override
    public void startTimer() {
        // Initiale Anzeige der Zeit
        miceTimerLabel.setText(timeRemaining + "s");

        // Timeline für den Countdown was pro Sekunde passiert
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            miceTimerLabel.setText(timeRemaining + "s");

            if (timeRemaining <= 0) {
                timer.stop(); // Timer stoppen
                handleTimeOut(); // Zeit ist abgelaufen
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE); // Timer läuft kontinuierlich
        timer.play(); // Timer starten
    }

    @Override
    public void resetTimer() {
        // Timer stoppen, bevor wir ihn zurücksetzen!
        // Timer wird nur einmal gestartet und nicht mehrfach!!
        // = verhindern Timer mehrere Instanzen od unkontrolliert springt

        if (timer != null) {
            timer.stop();
        }

        // Zeit zurücksetzen und Timer erneut starten
        timeRemaining = timeSet;
        startTimer();
    }

    @Override
    //was passiert wenn zeit vorbei
    public void handleTimeOut() {
        // Frage als falsch werten und zur nächsten wechseln
        markQuestionAsWrong();
        showFeedback("SQUEAK SQUEAK SQUEAK!!!", false);

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        miceAnswer1.setDisable(true);
        miceAnswer2.setDisable(true);
        miceAnswer3.setDisable(true);
        miceAnswer4.setDisable(true);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        loadNewQuestion();
    }


    // ------------ QUESTION LOADING and DISPLAYING ------------
    // die Fragen werden geladen dafür wird der QuestionLoader verwendet
    public void loadQuestions(String jsonFilePath) {
        // liste wird befüllt mit all den Fragen vom Json File
        questionsJson = QuestionLoader.loadQuestionsFromJson(jsonFilePath);

        if (questionsJson == null || questionsJson.isEmpty()) { //Fehlermeldung
            System.out.println("Error loading the questions.");
        }
    }

    // Logik/Vorbereitung zum Laden der nächsten Frage
    public void loadNewQuestion() {

        timer.pause();
        questionsAsked++; // Zähler für gestellte Fragen erhöhen

        // wenn noch leben und fragen da sind
        if (lives > 0 && questionsAsked <= questionsJson.size()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(3)); // 3 Sekunden Pause

            pause.setOnFinished(event -> {
                // Nach der Pause: neue Frage anzeigen und antwortbutton wieder ermöglichen
                miceAnswer1.setDisable(false);
                miceAnswer2.setDisable(false);
                miceAnswer3.setDisable(false);
                miceAnswer4.setDisable(false);
                //index erhöhen
                currentQuestionIndex++;
                //feedback wieder wegmachen
                miceFeedbackLabel.setVisible(false);
                //fragenanzahl aktualisieren

                resetAnswerButtonColors(); //farben der buttons zurücksetzen
                resetTimer(); // Timer zurücksetzen
                displayCurrentQuestion(); // Nächste Frage anzeigen
            });

            pause.play();
        } else { //wenn entweder leben oder fragen aufgebraucht
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                timer.pause();
                switchScene();
            });
            pause.play();
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
            miceQuestionLabel.setText(currentQuestion.getQuestionText());

            // Antwortmöglichkeiten wurden randomized in JsonQuestionsClass hier nur Buttons zugewiesen
            List<String> allAnswers = currentQuestion.getAllAnswers();
            miceAnswer1.setText(allAnswers.get(0));
            miceAnswer2.setText(allAnswers.get(1));
            miceAnswer3.setText(allAnswers.get(2));
            miceAnswer4.setText(allAnswers.get(3));
        } else {
            timer.pause();
            switchScene();
        }
    }


    // ------------ ANSWER HANDELING + CORRECTION ------------
    // kontrollieren ob die givenAnswer stimmt oder nicht
    @Override
    public void checkAnswer(String givenAnswer) {
        // man holt sich hier die aktuelle frage
        TutorialQuestions currentQuestion = questionsJson.get(currentQuestionIndex);
        // hier schaut man nach ob von der aktuellen frage die correct antwort die gegebene Antwort ist
        // die givenAnswer kommt von der handleAnswerButtonClick() methode
        if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(givenAnswer)) {
            markQuestionAsRight(); // wenn ja dann soll als richtig gewertet werden
            showFeedback("cin cin cin", true);
        } else {
            markQuestionAsWrong(); // wenn nein dann soll als falsch gewertet werden
            showFeedback("SQUEAK PIEP CUI-CUI!!!", false);
        }
    }

    // Ablauf was passiert wenn ein Antwort-Button vom User ausgewählt wurde
    @FXML
    @Override
    public void handleAnswerButtonClick(ActionEvent mainEvent) {
        Button clickedButton = (Button) mainEvent.getSource(); //hol dir den Button
        String selectedAnswer = clickedButton.getText(); //hol dir den Text vom Button

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        miceAnswer1.setDisable(true);
        miceAnswer2.setDisable(true);
        miceAnswer3.setDisable(true);
        miceAnswer4.setDisable(true);

        // Überprüfen, ob die Antwort korrekt ist
        checkAnswer(selectedAnswer);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        // Frage als beantwortet markieren und zur nächsten Frage wechseln
        loadNewQuestion();
    }

    // anzeigen feedback wenn richtig dann grün und wenn nicht dann rot
    @Override
    public void showFeedback(String feedback, boolean isCorrect) {
        miceFeedbackLabel.setText(feedback);
        miceFeedbackLabel.setStyle(isCorrect ? "-fx-text-fill: #5cd686;" : "-fx-text-fill: #e85c6c;");
        miceFeedbackLabel.setVisible(true);  // Feedback anzeigen
    }

    // was passiert wenn frage falsch beantwortet wurde
    private void markQuestionAsWrong() {
        lives--;
        QuizBase.super.markQuestionAsWrong(getLives(), miceLivesLabel);
    }

    // was passiwer wenn frage richtig beantwortet wurde
    private void markQuestionAsRight() {
        points += 1000;
        rightOnes++;
        QuizBase.super.markQuestionAsRight(getPoints(), getRightOnes(), micePointsLabel);
    }

    // richtig und falsch buttons anzeigen
    @Override
    public void setAnswerButtonColors() {
        // Über alle Antwort-Buttons iterieren und die Farben ändern
        Button[] answerButtons = {miceAnswer1, miceAnswer2, miceAnswer3, miceAnswer4};

        // hol dir jeden einzelnen button
        for (Button button : answerButtons) {
            // hol dir den text
            String answer = button.getText();

            // hol dir die aktuelle frage
            TutorialQuestions currentQuestion = questionsJson.get(currentQuestionIndex);

            // schauen ob button text der richtigen antwort von frage entspricht
            if (answer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
                button.setStyle("-fx-text-fill: #5cd686;");  // Richtige Antwort grün
            } else {
                button.setStyle("-fx-text-fill: #e85c6c;");  // Falsche Antwort rot
            }
        }
    }

    // buttons zurücksetzen
    private void resetAnswerButtonColors() {
        QuizBase.super.resetAnswerButtonColors(miceAnswer1, miceAnswer2, miceAnswer3, miceAnswer4);
    }


    // ------------ GETTER ------------
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


    // ------------ EXIT ------------
    public void onMiceExit(ActionEvent event) {
        QuizBase.super.onExit(event, timer); // Ruft die default-Methode aus dem Interface auf
    }


    // ------------ ENDSCREEN ------------
    private void showEndScreen() {
        // brauchen wir hier nicht
    }

    private void switchScene() {
        try {
            // Lade Main Menu (FXML-Datei)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/main_menu.fxml"));
            Parent mainRoot = loader.load();

            // MainMenuController holen
            MainMenuController mainMenuController = loader.getController();
            mainMenuController.setMrSqueak();

            Stage stage = (Stage) miceQuestionLabel.getScene().getWindow();

            // Szene wechseln
            Scene winScene = new Scene(mainRoot);
            stage.setScene(winScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}