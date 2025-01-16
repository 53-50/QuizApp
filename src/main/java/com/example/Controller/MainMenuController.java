package com.example.Controller;

//  Controller steuert die Logik und Benutzerinteraktionen für das Hauptmenü

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

public class MainMenuController {

    public RadioButton rbTutorial;

    // benötigt für Schriftart umschalten
    @FXML
    private Button firstFont;
    @FXML
    private Button secondFont;
    @FXML
    private VBox root;

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
    @FXML
    private Button inputTextFieldButton;
    @FXML
    private Button unlockInputTextFieldButton;

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

        //damit das Eingabefeld nicht gleich fokussiert ist und man den Prompttext lesen kann
        inputTextField.setFocusTraversable(false);

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

    public String getSelectedDifficultyMMC() {
        //User wählt schwierigkeit aus, nach Klick auf "Quiz-Play" wird difficulty an QuizController übergeben
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
        //User wählt schwierigkeit aus, nach Klick auf "Quiz-Play" wird difficulty an QuizController übergeben
        String selectedDifficultyMMC = getSelectedDifficultyMMC();

        if (selectedDifficultyMMC.equals("easy") || selectedDifficultyMMC.equals("medium") || selectedDifficultyMMC.equals("hard")){
            try {
                // Load the Quiz layout
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/quiz_layout.fxml"));
                Parent quizRoot = loader.load();

                // Get the QuizController
                QuizController quizController = loader.getController();
                // damit der playername auch beim quizspielen übergeben wird
                quizController.setPlayerName(currentname);
                quizController.setDifficulty(getSelectedDifficultyMMC()); //Doppelt, why??

                selectedDifficultyMMC = getSelectedDifficultyMMC();
                quizController.setDifficulty(selectedDifficultyMMC); // Pass the selected difficulty

                // Start the quiz (load the first question)
                quizController.loadNewQuestion(true); // <-- Call it explicitly here

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
        alert.setTitle("Lernmodus");
        alert.setHeaderText(null);
        alert.setContentText("Willkommen zum Lernmodus !\n\nBitte lade zuerst ein Template herunter, falls du noch keines " +
                "hast und trage deine Fragen in der Spalte <Fragen> und Antworten in der Spalte <Antworten> ein.\n\nAnschließend " +
                "lade das ausgefüllte Template hoch. Das Spiel startet automatisch nach dem hochladen\n\nViel Spaß beim lernen :)");

        try {
            ImageView customIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/KLKM_Logo.png")));
            customIcon.setFitWidth(50);
            customIcon.setFitHeight(50);
            alert.setGraphic(customIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ButtonType uploadButton = new ButtonType("Hochladen");
        ButtonType downloadTemplateButton = new ButtonType("Template herunterladen");
        ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(uploadButton, downloadTemplateButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == uploadButton) {
                uploadCsvFile();
            } else if (response == downloadTemplateButton) {
                downloadCsvTemplate();
            }
        });
    }

    @FXML
    private void uploadCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));
        Stage stage = (Stage) lernmodusButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            uploadedCsvFile = selectedFile;
            startLernmodus();
        } else {
            showAlert("Fehler", "Keine Datei ausgewählt. Bitte lade zuerst eine Datei hoch");
        }
        //uploadedCsvFile = fileChooser.showOpenDialog(stage);
    }

    private void downloadCsvTemplate() {
        String templateFileName = "/CSV Template/Lernmodus_CSV_Template.csv";
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialFileName("Lernmodus_CSV_Template.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));

        Stage stage = new Stage();
        File targetFile = fileChooser.showSaveDialog(stage);

        if (targetFile != null) {
            try (InputStream inputStream = getClass().getResourceAsStream(templateFileName);
                 OutputStream outputStream = new FileOutputStream(targetFile)) {

                if (inputStream == null) {
                    showAlert("Fehler", "Das Template konnte nicht gefunden werden.");
                    return;
                }

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                showAlert("Erfolg", "Das Template wurde erfolgreich heruntergeladen: " + targetFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Fehler", "Das Template konnte nicht heruntergeladen werden.");
            }
        }
        showCsvUploadDialog();
    }

    private void startLernmodus() {
        if (uploadedCsvFile != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/lernmodus_layout.fxml"));
                Parent lernmodusRoot = loader.load();

                LernmodusController lernmodusController = loader.getController();
                lernmodusController.loadQuestionsFromCsv(uploadedCsvFile);

                Stage stage = (Stage) lernmodusButton.getScene().getWindow();
                stage.setScene(new Scene(lernmodusRoot));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Fehler", "Ein Problem ist beim Laden des Lernmodus aufgetreten.");
            }
        } else {
            showAlert("Fehler", "Keine CSV-Datei vorhanden. Bitte lade zuerst eine Datei hoch");
        }
    }

    @FXML
    private void onExitClick(ActionEvent event) {
        // Fenster schließen
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /*
    private void loadQuizScene(ActionEvent event, String difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/quiz_layout.fxml"));
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
*/

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
            inputTextField.setDisable(true);

            unlockInputTextFieldButton.setDisable(false);
            inputTextFieldButton.setDisable(true);
        }
    }

    // was passiert wenn man auf den UnlockButton klickt
    @FXML
    public void onButtonClickUnlock(ActionEvent event) {
        //input wird wieder möglich aber zurückgesetzt
        inputTextField.setDisable(false);
        unlockInputTextFieldButton.setDisable(true);
        inputTextFieldButton.setDisable(false);
        inputTextField.clear();
        currentname = null;
        tutorialSpielen.setDisable(true);
        spielStarten.setDisable(true);

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
        switchScene(event, "/Layouts/tutorial_layout.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*
    // schriftartenwechsel gilt nur für Startseite
    public void switchFirstFont(ActionEvent actionEvent) {
        root.getStyleClass().removeAll("second-font");
        root.getStyleClass().add("first-font");
        secondFont.setDisable(false);
        firstFont.setDisable(true);
    }

    // schriftartenwechsel gilt nur für Startseite
    public void switchSecondFont(ActionEvent actionEvent) {
        root.getStyleClass().removeAll("first-font");
        root.getStyleClass().add("second-font");
        firstFont.setDisable(false);
        secondFont.setDisable(true);
    }
     */
}
