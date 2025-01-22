package com.example.Controller;

import com.example.Interface.ControllerBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;

public class WinLoseController {

    @FXML
    private Label modeLabel;
    @FXML
    private Label pointsLabel;
    @FXML
    private Label livesLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label rightOnesLabel;
    @FXML
    private Text storyTextLabel;
    @FXML
    private Button highscoreButton;

    // namen
    @FXML
    private Label nameLabel;
    private String playerName;

    // Infos von Controller holen damit man Statistik befüllen kann
    private ControllerBase controller;

    // damit der modus angezeigt werden kann
    private String mode;
    private String difficulty;

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    // controller setzten: tutorial oder quiz?
    public void setQuizController(ControllerBase controller) {
        this.controller = controller;

        if (controller instanceof QuizController) { // wenn es ein QuizController ist
            difficulty = ((QuizController) controller).getDifficultyQC(); //hol dir aktuelle difficulty
            if (difficulty == null || difficulty.isEmpty()) { // Fehler Handeling
                difficulty = "Error";
            }
            mode = "Quiz " + difficulty;  // setze die Variable mode
            highscoreButton.setVisible(true);
        } else if (controller instanceof TutorialController) { //wenn tutorial dann wird geschichte angezeigt
            storyTextLabel.setText("As we now know, she arrived a few years too early, " +
                            "missing the historic achievement of becoming the first cheese connoisseur in space. " +
                            "Tragically, her dream was cut short by human error. Let us take a moment to honor " +
                            "this unknown mouse, who gave her life in pursuit of her heroic aspirations. "+
                            "THE END ");
            storyTextLabel.setVisible(true);
            mode = "Tutorial"; // setze die Variable mode
        } else if (controller instanceof LearnModeController){ // wenn lernmodus
            mode = "Learning"; // setze die Variable mode
        }

    }


// ------------ DISPLAY LOGIC ------------
    public void displayMode() {
        modeLabel.setText("Mode: " + mode);
    }

    public int displayPoints() {
         int finalPoints = controller.getPoints();

        // wenn noch leben übrig sind => pro Leben +50 Punkte
        if (controller.getLives() > 0) {
            finalPoints += controller.getLives() * 50;
        }
        pointsLabel.setText("Points: " + finalPoints);
        return finalPoints;
    }

    public void displayRight() {
        int rightOnes = controller.getRightOnes();
        rightOnesLabel.setText("Right Answers: " + rightOnes + "/" + controller.getQuestions());
    }

    public void displayLives() {
        int finalLives = controller.getLives();

        if (finalLives < 0) {
            livesLabel.setText("Remaining Lives: None");
        } else {
            livesLabel.setText("Remaining Lives: " + finalLives);
        }
    }

    public void displayWinOrLose() {
        if (controller.getLives() > 0) {
            endLabel.setText("You Won! Congratulations little Mouse!");
        } else {
            endLabel.setText("You died like the mouse! :(");
        }
    }

    public void displayNameLabel() {
        nameLabel.setText(playerName);
    }


// ------------ BUTTON LOGIC (Retry, Home, Highscore) ------------

    // allgemeiner scene switch
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            //equals funktioniert mit String gut - muss man nicht overridden weil keine extra Logik benötigt
            if (fxmlPath.equals("/Layouts/tutorial_layout.fxml")) { // wenn es tutorial ist
                TutorialController tutorialController = loader.getController();
                tutorialController.setPlayerName(playerName); // Name wieder geben
            }

            if (fxmlPath.equals("/Layouts/quiz_layout.fxml")) { // wenn es quiz ist
                QuizController quizController = loader.getController();
                quizController.setPlayerName(playerName); // Name wieder geben
                quizController.setDifficulty(difficulty); // schwierigkeit wieder geben
            }

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    // spiel nochmal spielen
    public void onRetryClick(ActionEvent event) {
        if (controller instanceof TutorialController) {
            switchScene(event, "/Layouts/tutorial_layout.fxml");

        } else if (controller instanceof QuizController) {
            switchScene(event, "/Layouts/quiz_layout.fxml");
            String name = playerName;
            int finalScore = displayPoints();

            // Difficulty aus QuizController holen (easy, medium, hard)
            String difficulty = ((QuizController) controller).getDifficultyQC();

            // Prüfen im Highscore Controller, ob Wert vorhanden und ggf. überschreiben
            HighscoreController.updateScoreIfBetter(name, finalScore, difficulty);

        } else if (controller instanceof LearnModeController) {
            switchScene(event, "/Layouts/learnmode_layout.fxml");

        } else {
            switchScene(event, "/Layouts/main_menu.fxml");

        }
    }


    @FXML
    private void onHomeClick(ActionEvent event) {
        // ins Main Menue wechseln
        switchScene(event, "/Layouts/main_menu.fxml");

        // Nur im Quiz-Modus in Highscore speichern
        if (controller instanceof QuizController) {
            String name = playerName;
            int finalScore = displayPoints();

            String difficulty = ((QuizController) controller).getDifficultyQC(); // "easy" / "medium" / "hard"

            // Eintrag in den Highscore
            HighscoreController.addScore(name, finalScore, difficulty);
        }
    }

    @FXML
    private void onHighscoreClick(ActionEvent event) {

        // Nur im Quiz-Modus in Highscore speichern
        if (controller instanceof QuizController) {

            String name = playerName;
            int finalScore = displayPoints();
            String difficulty = ((QuizController) controller).getDifficultyQC();

            HighscoreController.addScore(name, finalScore, difficulty);
        }

        // in Highscore wechseln
        switchScene(event, "/Layouts/highscore_layout.fxml");
    }

}
