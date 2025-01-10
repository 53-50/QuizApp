package com.example.Controller;

import com.example.Questions.QuestionLoader;
import com.example.Questions.TutorialQuestions;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.animation.PauseTransition;

public class TutorialController {

    @FXML
    private Label tutorialLeben;

    @FXML
    private Label tutorialPunkte;

    @FXML
    private Label tutorialTimer;

    @FXML
    private Button tutorialantwort1;

    @FXML
    private Button tutorialantwort2;

    @FXML
    private Button tutorialantwort3;

    @FXML
    private Button tutorialantwort4;

    @FXML
    private Label tutorialfrage;

    @FXML
    private Label fragenanzahl;

    @FXML
    private Label tutorialFeedback;

    //der Timer
    private Timeline timer;
    // Zeit pro Frage in Sekunde
    private int timeRemaining = 15;

    // festlegen der Rahmenbedingungen
    private int punkte;
    private int leben = 3;
    final private int questions = 6;
    private int rightOnes = 0;
    private int questionsasked = 1;

    //jsonfile reinladen
    private List<TutorialQuestions> questionsjson;
    private int currentQuestionIndex = 0; //brauchen wir damit die fragen nacheinander ausgelesen werden
    private String jsonFilePath = "src/main/resources/questions/questions.json";

    //popups
    private Queue<String> popupMessages;  // Warteschlange für Pop-up-Nachrichten

    //namen übergabe
    private String playerName;

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    @FXML
    public void initialize() {
        // Fragen aus der JSON-Datei laden
        loadQuestions(jsonFilePath);

        // Timer initialisieren
        startTimer();

        // Falls Fragen geladen wurden, die erste Frage anzeigen
        if (questionsjson != null && !questionsjson.isEmpty()) {
            displayCurrentQuestion();
        } else {
            System.out.println("Keine Fragen gefunden!");
        }

        // Rahmenbedingungen setzen
        tutorialPunkte.setText(Integer.toString(punkte));
        tutorialLeben.setText(Integer.toString(leben));
        fragenanzahl.setText(Integer.toString(questionsasked) + "/" + Integer.toString(questions));

        //Queue Liste erstellen für Tutorial Pop-Ups
        popupMessages = new LinkedList<>();

        // Pop-up-Nachrichten hinzufügen
        popupMessages.add("1/3 Tutorial\n\nEs war einmal eine Maus, die sich nichts sehnlicher wünschte, " +
                "als den Käse ihrer Träume zu finden – und zwar im Weltall. " +
                "Mit einer Rakete und unerschütterlichem Mut machte sie sich " +
                "auf den Weg, um Geschichte zu schreiben.\n" +
                "\n" +
                "Bist du bereit, dein Wissen über diese mutige Maus zu testen?");
        popupMessages.add("2/3 Tutorial\n\nDu gewinnst einen Punkt dazu, wenn du die Maus beeindruckt hast.\n\n" +
                "Du verlierst ein Leben wenn du die Maus enttäuschst.\n\n" + "Es liegt an dir!");
        popupMessages.add("3/3 Tutorial\n\nAchte aber auf den Timer! Du hast pro Frage nur " + timeRemaining + " Sekunden.\n\n" +
                "Wenn du zu lange brauchst enttäuschst du die Maus.");

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


    private void startTimer() {
        // Initiale Anzeige der Zeit
        tutorialTimer.setText(timeRemaining + "s");

        // Timeline für den Countdown was pro Sekunde passiert
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            tutorialTimer.setText(timeRemaining + "s");

            if (timeRemaining <= 0) {
                timer.stop(); // Timer stoppen
                handleTimeOut(); // Zeit ist abgelaufen
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE); // Timer läuft kontinuierlich
        timer.play(); // Timer starten
    }

    //was passiert wenn zeit vorbei
    private void handleTimeOut() {
        System.out.println("Zeit abgelaufen! Frage wird als falsch gewertet.");
        // Frage als falsch werten und zur nächsten wechseln
        markQuestionAsWrong();
        showFeedback("Zeit abgelaufen! Wegen dir stirbt die Maus :(", false);

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        tutorialantwort1.setDisable(true);
        tutorialantwort2.setDisable(true);
        tutorialantwort3.setDisable(true);
        tutorialantwort4.setDisable(true);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        loadNextQuestion();
    }

    // die Fragen werden geladen dafür wird der QuestionLoader verwendet
    public void loadQuestions(String jsonFilePath) {
        questionsjson = QuestionLoader.loadQuestionsFromJson(jsonFilePath);
        if (questionsjson == null || questionsjson.isEmpty()) { //Fehlermeldung
            System.out.println("Fehler beim Laden der Fragen.");
        }
    }

    // damit die frage welche dran ist mit den dazugehörigen antworten angezeigt wird
    private void displayCurrentQuestion() {
        // nachgeschaut ob noch fragen da sind
        if (currentQuestionIndex < questionsjson.size()) {
            //aktuelle frage wird sich geholt
            TutorialQuestions currentQuestion = questionsjson.get(currentQuestionIndex);

            // Frage anzeigen
            tutorialfrage.setText(currentQuestion.getQuestion());

            // Antwortmöglichkeiten wurden randomized in TutorialQuestions hier nur Buttons zugewiesen
            List<String> allAnswers = currentQuestion.getAllAnswers();
            tutorialantwort1.setText(allAnswers.get(0));
            tutorialantwort2.setText(allAnswers.get(1));
            tutorialantwort3.setText(allAnswers.get(2));
            tutorialantwort4.setText(allAnswers.get(3));
        } else {
            showEndScreen(); // Falls keine weiteren Fragen vorhanden sind zeig das Ende
        }
    }

    // Ablauf was passiert wenn ein Antwort-Button vom User ausgewählt wurde
    @FXML
    private void handleAnswerButtonClick(ActionEvent mainevent) {
        Button clickedButton = (Button) mainevent.getSource(); //hol dir den Button
        String selectedAnswer = clickedButton.getText(); //hol dir den Text vom Button

        //deaktivier die Buttons damit nichts weiter gedrückt werden kann
        tutorialantwort1.setDisable(true);
        tutorialantwort2.setDisable(true);
        tutorialantwort3.setDisable(true);
        tutorialantwort4.setDisable(true);

        // Überprüfen, ob die Antwort korrekt ist
        checkAnswer(selectedAnswer);

        // Alle Buttons durchlaufen und die Farben ändern je nach richtig oder falsch
        setAnswerButtonColors();

        // Frage als beantwortet markieren und zur nächsten Frage wechseln
        loadNextQuestion();
    }

    // anzeigen ob richtig oder falsch
    private void showFeedback(String feedback, boolean isCorrect) {
        tutorialFeedback.setText(feedback);
        tutorialFeedback.setStyle(isCorrect ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        tutorialFeedback.setVisible(true);  // Feedback anzeigen
    }

    // kontrollieren ob die givenAnswer stimmt oder nicht
    public void checkAnswer(String givenAnswer) {
        TutorialQuestions currentQuestion = questionsjson.get(currentQuestionIndex);
        if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(givenAnswer)) {
            markQuestionAsRight();
            showFeedback("Richtig! Du hebst ab wie eine Rakete :)", true);
        } else {
            markQuestionAsWrong();
            showFeedback("Wegen dir stirbt die Maus :(", false);
        }
    }

    private void markQuestionAsWrong() {
        // Logik zum Markieren der aktuellen Frage als falsch
        leben--;
        tutorialLeben.setText(Integer.toString(leben) + " Leben");
    }

    private void markQuestionAsRight() {
        // Logik zum Markieren der aktuellen Frage als falsch
        punkte++;
        tutorialPunkte.setText(Integer.toString(punkte)+ " Punkte");
        rightOnes++;
    }

    private void loadNextQuestion() {
        // Logik zum Laden der nächsten Frage

        timer.pause();

        questionsasked++; // Zähler für gestellte Fragen erhöhen

        if (leben > 0 && questionsasked <= questionsjson.size()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(3)); // 3 Sekunden Pause

            pause.setOnFinished(event -> {
                // Nach der Pause: neue Frage anzeigen und antwortbutton wieder ermöglichen
                tutorialantwort1.setDisable(false);
                tutorialantwort2.setDisable(false);
                tutorialantwort3.setDisable(false);
                tutorialantwort4.setDisable(false);
                //index erhöhen
                currentQuestionIndex++;
                //feedback wieder wegmachen
                tutorialFeedback.setVisible(false);
                //fragenanzahl aktualisieren
                fragenanzahl.setText(Integer.toString(questionsasked) + "/" + Integer.toString(questions));

                resetAnswerButtonColors(); //farben der buttons zurücksetzen
                resetTimer(); // Timer zurücksetzen
                displayCurrentQuestion(); // Nächste Frage anzeigen
            });

            pause.play();
        } else {
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
            showEndScreen();
            });
            pause.play();
        }
    }

    // richtig und falsch buttons anzeigen
    private void setAnswerButtonColors() {
        // Über alle Antwort-Buttons iterieren und die Farben ändern
        Button[] answerButtons = {tutorialantwort1, tutorialantwort2, tutorialantwort3, tutorialantwort4};

        for (Button button : answerButtons) {
            String answer = button.getText();

            TutorialQuestions currentQuestion = questionsjson.get(currentQuestionIndex);
            if (answer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
                button.setStyle("-fx-text-fill: green;");  // Richtige Antwort grün
            } else {
                button.setStyle("-fx-text-fill: red;");  // Falsche Antwort rot
            }
        }
    }

    private void resetAnswerButtonColors() {
        // Alle Buttons wieder auf die Standardfarbe setzen
        Button[] answerButtons = {tutorialantwort1, tutorialantwort2, tutorialantwort3, tutorialantwort4};
        for (Button button : answerButtons) {
            button.setStyle("-fx-text-fill: black;"); // Standardfarbe zurücksetzen
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

    //GETTER
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


    //endscreen anzeigen
    private void showEndScreen() {
        try {
            // Lade den Win Screen (FXML-Datei)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/win_lose_layout.fxml"));
            Parent winRoot = loader.load();

            // WinScreenController holen
            WinLoseLayoutController winLoseLayoutControllerController = loader.getController();

            // Referenz zum aktuellen QuizController und den Playernamen übergeben
            winLoseLayoutControllerController.setQuizController(this);
            winLoseLayoutControllerController.setPlayerName(playerName);

            // Anzeige der Punktzahl aktualisieren
            winLoseLayoutControllerController.displayPunkte();
            winLoseLayoutControllerController.displayLeben();
            winLoseLayoutControllerController.displayWinOrLose();
            winLoseLayoutControllerController.displayRight();
            winLoseLayoutControllerController.displayNamenLabel();

            // Szene wechseln
            Stage stage = (Stage) tutorialfrage.getScene().getWindow();
            Scene winScene = new Scene(winRoot);
            stage.setScene(winScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // BUTTON: SCHLIEßEN

    //allgemeinen Scenen switchen festlegen
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {

            //sich neues FXML holen
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // wenn schließen button gedrückt wird
    public void onTutorialExit(ActionEvent event) {

        // braucht man damit popup auftaucht und er nach bist du sicher das spiel beendet wird
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/popups.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupMessage("Bist du sicher?");  // Nachricht setzen

            Stage popupStage = new Stage();
            popupStage.setTitle("Tutorial Pop-up");
            popupStage.setScene(new Scene(root));

            if (timer != null) {
                timer.pause();  // Pausiert den Timer
            }

            // Zeigt das Pop-up an und wartet, bis es geschlossen wird
            popupStage.initModality(Modality.APPLICATION_MODAL);  // Macht das Pop-up modal (blockiert das Hauptfenster)
            popupStage.showAndWait();

            if(popupController.isUserSure()) {
                // Wenn das Pop-up geschlossen wird, führe das aus:
                switchScene(event, "/Layouts/main_menu.fxml");
            } else {
                timer.play();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
