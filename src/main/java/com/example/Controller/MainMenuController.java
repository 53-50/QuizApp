package com.example.Controller;

//  Controller steuert die Logik und Benutzerinteraktionen für das Hauptmenü

import com.example.Questions.LernmodusQuestion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainMenuController {

    public RadioButton rbTutorial;
    @FXML
    private RadioButton rbEasy;
    @FXML
    private RadioButton rbMedium;
    @FXML
    private RadioButton rbHard;
    @FXML
    private ToggleGroup difficultyToggleGroup;

    //für Text Eingabe benötigt
    @FXML
    private TextField inputTextField;

    private static final int MAX_CHARACTERS = 15;

    @FXML
    private Label errorLabel;

    @FXML
    private Button spielStarten;

    @FXML
    private Button tutorialSpielen;

    public String currentname;

    private File uploadedCsvFile;

    @FXML
    private Button lernmodusButton;


    @FXML
    public void initialize() {

        tutorialSpielen.setDisable(true);
        spielStarten.setDisable(true);

        difficultyToggleGroup = new ToggleGroup();

        // Zweck: Die RadioButtons sollen gruppiert werden, sodass immer nur eine Option ausgewählt werden kann.

        rbEasy.setToggleGroup(difficultyToggleGroup);
        rbMedium.setToggleGroup(difficultyToggleGroup);
        rbHard.setToggleGroup(difficultyToggleGroup);

        // Optional: Standardmäßig z.B. rbEasy auswählen
        rbEasy.setSelected(true);

        // Beschränkungen von den Zeichen indem Änderungen an der Texteingabe überwacht
        // und überflüssige Zeichen abschneidet

        // textProperty() gibt eine Bindung (eine sogenannte Property) des Textes zurück, der sich im TextField befindet.
        // Mit dieser Property können wir Veränderungen am Textfeld überwachen.

        // addListener fügt Listener hinzu.
        // Listener ist Methode, die bei einer Veränderung ausgeführt wird. reagiert auf Änderungen der textProperty()

        // observable: überwachte Objekt (in diesem Fall textProperty des Textfeldes). Für unseren Fall nicht direkt genutzt.
        // oldValue: Der alte Wert (Text), bevor die Änderung erfolgte.
        // newValue: Der neue Wert (Text), nachdem die Änderung eingetreten ist.
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            //Bedingung prüft, ob Länge des neuen Texts (nach der Änderung)
            // maximale erlaubte Anzahl an Zeichen (MAX_CHARACTERS) überschreitet.
            if (newValue.length() > MAX_CHARACTERS) {
                // Kürze den Text, wenn er zu lang ist
                inputTextField.setText(newValue.substring(0, MAX_CHARACTERS));
            }
        });
    }

    private String getSelectedDifficulty() {
        // Check which radio button is selected
        if (difficultyToggleGroup.getSelectedToggle() == rbEasy) {
            return "easy";
        } else if (difficultyToggleGroup.getSelectedToggle() == rbMedium) {
            return "medium";
        } else if (difficultyToggleGroup.getSelectedToggle() == rbHard) {
            return "hard";
        }
        return "easy"; // Default to "easy" if none selected (shouldn't happen with rbEasy setSelected(true))
    }


    @FXML
    private void onStartQuizClick(ActionEvent event) {
        String selectedDifficulty = getSelectedDifficulty();

        if (selectedDifficulty.equals("learn")) {
            // Wenn Lernmodus ausgewählt ist, lade die Lernmodus-Szene
            /*loadLearnModeScene(event);*/
        } else if (selectedDifficulty.equals("easy") || selectedDifficulty.equals("medium") || selectedDifficulty.equals("hard")){
            // Andernfalls starte den API-basierten Quiz-Modus
            // loadQuizScene(event, selectedDifficulty);

            try {
                // Load the Quiz layout
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz_layout.fxml"));
                Parent quizRoot = loader.load();

                // Get the QuizController
                QuizController quizController = loader.getController();

                selectedDifficulty = getSelectedDifficulty();
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
    }

//    @FXML
//    private void onStartQuizClick(ActionEvent event) {
//        try {
//            // Load the Quiz layout
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz_layout.fxml"));
//            Parent quizRoot = loader.load();
//
//            // Get the QuizController
//            QuizController quizController = loader.getController();
//
//            String selectedDifficulty = getSelectedDifficulty();
//            quizController.setDifficulty(selectedDifficulty); // Pass the selected difficulty
//
//            quizController.resetQuiz(); // Reset the quiz for a new game
//
//            // Start the quiz (load the first question)
//            quizController.loadNewQuestion(); // <-- Call it explicitly here
//
//            // Switch to the quiz scene
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(quizRoot));
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void showCsvUploadDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CSV-Datei hochladen");
        alert.setHeaderText(null);
        alert.setContentText("Bitte .csv file hochladen.");

        ButtonType uploadButton = new ButtonType("Hochladen");
        ButtonType startButton = new ButtonType("Starten");

        alert.getButtonTypes().setAll(uploadButton, startButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == uploadButton) {
                uploadCsvFile();
            } else if (response == startButton) {
                startProcess();
            }
        });
    }

    private void uploadCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));
        Stage stage = new Stage();
        uploadedCsvFile = fileChooser.showOpenDialog(stage);
    }

    private void startProcess() {
        if (uploadedCsvFile != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lernmodus_layout.fxml"));
                Parent lenrmodusRoot = loader.load();

                LernmodusController lernmodusController = loader.getController();
                lernmodusController.loadQuestionsFromCsv(uploadedCsvFile);

                Stage stage = (Stage) lernmodusButton.getScene().getWindow();
                stage.setScene(new Scene(lenrmodusRoot));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

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

    // Namen Eingabe - Wird mal nur gespeichert in der Variable und dann bei Enter in der Console ausgegeben
    @FXML
    public void onButtonClick(ActionEvent event) {

        if (inputTextField.getText().trim().isEmpty()) {
            // Zeige eine Fehlermeldung an
            errorLabel.setVisible(true);
            inputTextField.setStyle("-fx-border-color: #ff0000;");
        } else { // Weiterverarbeiten der Eingabe
            errorLabel.setVisible(false);
            inputTextField.setStyle("-fx-border-color: black;");
            String name = inputTextField.getText();
            currentname = name;
            System.out.println(currentname);
            tutorialSpielen.setDisable(false);
            spielStarten.setDisable(false);


        }
    }


    //FÜR TUTORIAL MODUS
    // Holt sich die Stage welche dann benötigt wird sobald ein AcionEvent gesetzt wird
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            TutorialController tutorialController = loader.getController();
            tutorialController.setPlayerName(currentname);

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // wenn TutorialMode geklickt wird gibt die Methode den fxml path an switchScene weiter
    public void onTutorialModeClick(ActionEvent event) {
        switchScene(event, "/tutorial_layout.fxml");
    }
}
