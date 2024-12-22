package com.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private RadioButton rbEasy;
    @FXML
    private RadioButton rbMedium;
    @FXML
    private RadioButton rbHard;
    @FXML
    private RadioButton rbLearnMode;

    private ToggleGroup difficultyToggleGroup;

    @FXML
    public void initialize() {
        difficultyToggleGroup = new ToggleGroup();

        rbEasy.setToggleGroup(difficultyToggleGroup);
        rbMedium.setToggleGroup(difficultyToggleGroup);
        rbHard.setToggleGroup(difficultyToggleGroup);
        rbLearnMode.setToggleGroup(difficultyToggleGroup);

        // Optional: Standardmäßig z.B. rbEasy auswählen
        rbEasy.setSelected(true);
    }

    @FXML
    private void onStartQuizClick(ActionEvent event) {
        // Prüfen, welcher RadioButton ausgewählt ist:
        if (rbLearnMode.isSelected()) {
            // Lernmodus starten
            loadLearnModeScene(event);
        } else {
            // Quiz starten mit passendem Schwierigkeitsgrad
            // (easy, medium, hard)
            String difficulty = "easy";  // Default
            if (rbMedium.isSelected()) {
                difficulty = "medium";
            } else if (rbHard.isSelected()) {
                difficulty = "hard";
            }

            loadQuizScene(event, difficulty);
        }
    }

    @FXML
    private void onExitClick(ActionEvent event) {
        // Fenster schließen
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadQuizScene(ActionEvent event, String difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz_layout.fxml"));
            Parent quizRoot = loader.load();

            // Controller holen, um den Schwierigkeitsgrad zu übergeben
            QuizController quizController = loader.getController();
            quizController.setDifficulty(difficulty);

            // Neue Szene erstellen
            Scene quizScene = new Scene(quizRoot);

            // Stage ermitteln
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(quizScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLearnModeScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lernmodus_layout.fxml"));
            Parent learnRoot = loader.load();

            // Falls es einen Lernmodus-Controller gibt, kannst du auch da was übergeben, z.B.:
            // LernmodusController lmController = loader.getController();
            // lmController.initSomething();

            Scene learnScene = new Scene(learnRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(learnScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
