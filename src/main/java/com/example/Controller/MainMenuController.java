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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

public class MainMenuController {

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
    private TextField inputTextField; //hier wird NamenText eingegeben
    @FXML
    private Button inputTextFieldButton; //hier wird die NamenEingabe bestätigt
    @FXML
    private Button unlockInputTextFieldButton; //hier wird die NamenEingabe wieder gelöscht

    private static final int MAX_CHARACTERS = 15; // Angabe von max. zulässigen Zeichen bei NamenEingabe

    public String currentName; //mithilfe dieser wird der Name an andere Controller übergeben

    @FXML
    private Label errorLabel;

    @FXML
    private Button playGame;

    @FXML
    private Button playTutorial;

    private File uploadedCsvFile;

    @FXML
    private Button learnModeButton;




    @FXML
    public void initialize() {

        //damit das Eingabefeld nicht gleich fokussiert ist und man den Prompttext lesen kann
        inputTextField.setFocusTraversable(false);

        //damit bevor nicht Namen angegeben die Modes die Namen benötigen nicht spielbar sind
        playTutorial.setDisable(true);
        playGame.setDisable(true);

        difficultyToggleGroup = new ToggleGroup();

        // Zweck: Die RadioButtons sollen gruppiert werden, sodass immer nur eine Option ausgewählt werden kann.

        rbEasy.setToggleGroup(difficultyToggleGroup);
        rbMedium.setToggleGroup(difficultyToggleGroup);
        rbHard.setToggleGroup(difficultyToggleGroup);

        // Optional: Standardmäßig z.B. rbEasy auswählen
        rbEasy.setSelected(true);

        // Beschränkungen von Zeichen indem Änderungen an Texteingabe überwacht
        // + überflüssige Zeichen abschneidet
        // textProperty() gibt Bindung ("Property") des Textes zurück, der sich im TextField befindet.
        // Mit Property kann Veränderungen am Textfeld überwacht werden

        // addListener fügt Listener hinzu
        // Listener ist Methode, die bei Veränderung ausgeführt wird, reagiert auf Änderungen d. textProperty()
        // observable: überwachte Objekt (textProperty des Textfeldes) => nicht direkt genutzt
        // oldValue: alte Wert, vor Änderung <=> newValue: neue Wert nach Änderung
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            //Bedingung prüft, ob Länge des neuen Texts (nach der Änderung)
            // maximale erlaubte Anzahl an Zeichen (MAX_CHARACTERS) überschreitet.
            if (newValue.length() > MAX_CHARACTERS) {
                // Kürze den Text, wenn er zu lang ist
                // inputTextField auf Text gesetzt => aus ersten MAX_CHARACTERS des Strings newValue besteht
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
                quizController.setPlayerName(currentName);
                quizController.setDifficulty(getSelectedDifficultyMMC()); //Doppelt, why??

               // selectedDifficultyMMC = getSelectedDifficultyMMC();
                //quizController.setDifficulty(selectedDifficultyMMC); // Pass the selected difficulty

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
        alert.setTitle("Learning Mode");
        alert.setHeaderText(null);
        alert.setContentText("Welcome to the Learning Mode !\n\nPlease download a template if you dont have one yet " +
                "and enter your questions in the column <Questions> and your answers in the column <Answers>.\n\nFinally " +
                "upload the filled out template. The Learning Mode will start automatically after the upload.\n\nHappy learning :)");

        try {
            ImageView customIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/KLKM_Logo.png")));
            customIcon.setFitWidth(50);
            customIcon.setFitHeight(50);
            alert.setGraphic(customIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ButtonType uploadButton = new ButtonType("Upload");
        ButtonType downloadTemplateButton = new ButtonType("Download Template");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

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
        Stage stage = (Stage) learnModeButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            uploadedCsvFile = selectedFile;
            startLearnMode();
        } else {
            showAlert("Error", "No file chosen. Please upload a file first.");
        }
    }

    private void downloadCsvTemplate() {
        String templateFileName = "/CSV Template/Learning Mode_CSV_Template.csv";
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialFileName("Learning Mode_CSV_Template.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = new Stage();
        File targetFile = fileChooser.showSaveDialog(stage);

        if (targetFile != null) {
            try (InputStream inputStream = getClass().getResourceAsStream(templateFileName);
                 OutputStream outputStream = new FileOutputStream(targetFile)) {

                if (inputStream == null) {
                    showAlert("Error", "Template could not be found.");
                    return;
                }

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                showAlert("Success", "The template was successfully downloaded: " + targetFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "The template could not be downloaded.");
            }
        }
        showCsvUploadDialog();
    }

    private void startLearnMode() {
        if (uploadedCsvFile != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/learnmode_layout.fxml"));
                Parent lernmodusRoot = loader.load();

                LearnModeController learnModeController = loader.getController();
                learnModeController.loadQuestionsFromCsv(uploadedCsvFile);

                if (learnModeController.getQuestions().isEmpty()) {
                    showAlert("Error", "The CSV-File has no valid data.");
                    return;
                }

                Stage stage = (Stage) learnModeButton.getScene().getWindow();
                stage.setScene(new Scene(lernmodusRoot));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "A problem occurred while loading the Learn mode.");
            }
        } else {
            showAlert("Error", "No CSV-File available. Please upload a file first.");
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

    // Namen Eingabe Handeling - Button Click "Enter"
    @FXML
    public void onButtonClick(ActionEvent event) {
        //wenn TextEingabeFeld leer ist = trim damit leerzeichen nicht zählen als eingabe
        if (inputTextField.getText().trim().isEmpty()) {
            // Zeige eine Fehlermeldung an
            errorLabel.setVisible(true);
            inputTextField.setStyle("-fx-border-color: #ff0000;");
        } else { // Weiterverarbeiten der Eingabe, wenn was drinnen ist
            errorLabel.setVisible(false); // Fehledermeldung wegmachen falls sie da stand
            inputTextField.setStyle("-fx-border-color: black;");
            currentName = inputTextField.getText(); // variable zuordnen
            playTutorial.setDisable(false); // tutorial modus möglich machen zum spielen
            playGame.setDisable(false); // quiz modus möglich machen zum spielen
            inputTextField.setDisable(true); //verhindern dass man nameneingabe ändern kann
            unlockInputTextFieldButton.setDisable(false); //ermöglichen eingabe zu löschen
            inputTextFieldButton.setDisable(true); // verhindern enter erneut zu drücken
        }
    }

    // was passiert wenn man auf UnlockButton klickt
    @FXML
    public void onButtonClickUnlock(ActionEvent event) {
        //input wird wieder möglich aber zurückgesetzt (gelöscht) - Ausgangssituation
        inputTextField.setDisable(false);
        unlockInputTextFieldButton.setDisable(true);
        inputTextFieldButton.setDisable(false);
        inputTextField.clear();
        currentName = null;
        playTutorial.setDisable(true);
        playGame.setDisable(true);
    }


    @FXML
    // wenn "TutorialMode" - Button geklickt wird -> scenen wechsel zu Tutorial_Layout.fxml
    public void onTutorialModeClick(ActionEvent event) {
        try {
            // FXMLLoader läd FXML Datei (tutorial_layout.fxml) => Layout von Tutorial Mode
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/tutorial_layout.fxml"));
            // liest die Datei und gibt zurück den Root Node vom Layout (Start von Scene)
            Parent root = loader.load();

            // loader.getController() = erhälts controller instance => erstellt + initialisiert von FXMLLoader
            TutorialController tutorialController = loader.getController();
            // dem Controller übergibst du über Methode "setPlayerName" die currentName Variable
            tutorialController.setPlayerName(currentName);

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
