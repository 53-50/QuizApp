package com.example.Controller;

import com.example.Questions.LernmodusQuestion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainMenuController {

    @FXML
    private RadioButton rbEasy;
    @FXML
    private RadioButton rbMedium;
    @FXML
    private RadioButton rbHard;
    @FXML
    private RadioButton rbLearnMode;
    @FXML
    private ToggleGroup difficultyToggleGroup;

    //für Text Eingabe benötigt
    @FXML
    private TextField inputTextField;

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

    private String getSelectedDifficulty() {
        // Check which radio button is selected
        if (difficultyToggleGroup.getSelectedToggle() == rbEasy) {
            return "easy";
        } else if (difficultyToggleGroup.getSelectedToggle() == rbMedium) {
            return "medium";
        } else if (difficultyToggleGroup.getSelectedToggle() == rbHard) {
            return "hard";
        } else if (difficultyToggleGroup.getSelectedToggle() == rbLearnMode) {
            return "learn";
        }
        return "easy"; // Default to "easy" if none selected (shouldn't happen with rbEasy setSelected(true))
    }


    @FXML
    private void onStartQuizClick(ActionEvent event) {
        String selectedDifficulty = getSelectedDifficulty();

        if (selectedDifficulty.equals("learn")) {
            // Wenn Lernmodus ausgewählt ist, lade die Lernmodus-Szene
            loadLearnModeScene(event);
        } else {
            // Andernfalls starte den API-basierten Quiz-Modus
            loadQuizScene(event, selectedDifficulty);
        }
    }

/*
    @FXML
    private void onStartQuizClick(ActionEvent event) {
        try {
            // Load the Quiz layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz_layout.fxml"));
            Parent quizRoot = loader.load();

            // Get the QuizController
            QuizController quizController = loader.getController();

            String selectedDifficulty = getSelectedDifficulty();
            quizController.setDifficulty(selectedDifficulty); // Pass the selected difficulty

            quizController.resetQuiz(); // Reset the quiz for a new game

            // Start the quiz (load the first question)
            quizController.loadNewQuestion(); // <-- Call it explicitly here

            // Switch to the quiz scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(quizRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

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


    // Methode zum Hochladen von Fragen (CSV-Datei)

    private String uploadedCsvFilePath;

    public void onUploadClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CSV-Datei hochladen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
             uploadedCsvFilePath = file.getAbsolutePath(); // Speichere den Pfad
            try {
                List<LernmodusQuestion> questions = LernmodusQuestion.importFromCsv(uploadedCsvFilePath);
                System.out.println("Fragen erfolgreich hochgeladen: " + questions.size());
            } catch (IOException e) {
                System.err.println("Fehler beim Hochladen der Datei: " + e.getMessage());
            }
        }
    }

    private void loadLearnModeScene(ActionEvent event) {
        if (uploadedCsvFilePath == null || uploadedCsvFilePath.isEmpty()) {
            System.err.println("Keine CSV-Datei hochgeladen. Der Lernmodus kann nicht gestartet werden.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lernmodus_layout.fxml"));
            Parent learnRoot = loader.load();

            // Übergabe des Dateipfads an den LernmodusController
            LernmodusController lmController = loader.getController();
            lmController.setCsvFilePath(uploadedCsvFilePath);

            Scene learnScene = new Scene(learnRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(learnScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode zum Herunterladen von Fragen (CSV-Datei speichern)
    public void onDownloadClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CSV-Datei speichern");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));

        // Speicherort auswählen
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                // Beispiel-Fragen generieren (in der Praxis könnten diese aus einer Datenquelle stammen)
                List<LernmodusQuestion> questions = List.of(
                        new LernmodusQuestion("1", "Was ist Java?", "Eine Programmiersprache",
                                List.of("Eine Insel", "Ein Kaffee", "Ein Auto"), "Programmierung"),
                        new LernmodusQuestion("2", "Was ist 2+2?", "4",
                                List.of("3", "5", "22"), "Mathematik")
                );

                // Fragen exportieren
                LernmodusQuestion.exportToCsv(file.getAbsolutePath(), questions);
                System.out.println("Fragen erfolgreich heruntergeladen.");
            } catch (IOException e) {
                System.err.println("Fehler beim Herunterladen der Datei: " + e.getMessage());
            }
        }
    }


    // Namen Eingabe - Wird mal nur gespeichert in der Variable und dann bei Enter in der Console ausgegeben
    @FXML
    public String onButtonClick(ActionEvent event) {
        String currentName = inputTextField.getText();

        System.out.println(currentName);

        return currentName;
    }

}
