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
import javafx.stage.Modality;

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
    private Button learnModeButton; // Button für den Lernmodus

    @FXML
    private Label mrSqueakLabel;
    @FXML
    private Button mrSqueakButton;


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

    // Methode zur Anzeige des CSV Dialogs
    @FXML
    private void showLearnModeDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/learnmode_dialog_layout.fxml"));
            Parent dialogRoot = loader.load();

            LearnmodeDialogController controller = loader.getController();
            controller.setListener(new LearnmodeDialogController.LearnmodeDialogListener() {
                @Override
                public void onUploadSelected() {
                    uploadCsvFile();
                }

                @Override
                public void onDownloadTemplateSelected() {
                    downloadCsvTemplate();
                }
            });

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Learning Mode");
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showPopup("The dialog could not be loaded", true);
        }
    }

    // Methode für den Fileupload
    @FXML
    private void uploadCsvFile() {
        // Neue Instanz von FileChooser wird erstellt damit der User die Möglichkeit hat
        // eine Datei von seinem Computer hoch zu laden
        FileChooser fileChooser = new FileChooser();
        // Zeigt dem User im Dialog nur csv files an
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));
        // Wandelt Ergebnis von getWindow() in Stage um
        // Ermittelt aktuelles Anwendungsfenster um Datei-Dialog innerhalb desselben Fensters zu öffnen
        Stage stage = (Stage) learnModeButton.getScene().getWindow();
        // Zeigt Datei-Dialog an und erwartet Benutzerinteraktion
        // Gibt die vom Benutzer ausgewählte Datei als File-Objekt zurück
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Überprüft ob eine Datei ausgewählt wurde
        if (selectedFile != null) {
            // Falls ja, dann wird die Datei der Klassenvariable übergeben und der Lernmodus
            // wird gestartet
            uploadedCsvFile = selectedFile;
            startLearnMode();
        } else {
            // Fehlermeldung im Falle einer Ausnahme
            showPopup("No file chosen. Please upload a file first.", true);
        }
    }

    private void downloadCsvTemplate() {
        // Pfad zum Template aus Projektstruktur definiert
        String templateFileName = "/CSV Template/Learning Mode_CSV_Template.csv";
        // Neue Instanz von FileChooser um Speicherort zu bestimmen
        FileChooser fileChooser = new FileChooser();

        // Standard Dateiname wird festgelegt
        fileChooser.setInitialFileName("Learning Mode_CSV_Template.csv");
        // Filter damit der user nur Dateien speichern kann mit der Endung .csv
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Neue Stage für den Dateidialog
        Stage stage = new Stage();
        // Öffnet Dialog indem Benutzer Speicherort und Namen für Datei wählen kann
        File targetFile = fileChooser.showSaveDialog(stage);

        // Prüft ob Benutzer tatsächlich Speicherort ausgewählt hat
        if (targetFile != null) {
            // Falls ja dann wird dem InputStream der Dateipfad aus der Projektstruktur übergeben
            try (InputStream inputStream = getClass().getResourceAsStream(templateFileName);
                 // Im OutputStream werden die Infos übergeben die der Benutzer im Dateidialog
                 // gewählt hat
                 OutputStream outputStream = new FileOutputStream(targetFile)) {

                // Fehlermeldung falls der Dateipfad in der Projektstruktur nicht gefunden werden kann
                if (inputStream == null) {
                    showPopup("Template could not be found.", false);
                    return;
                }

                // Temporärer Speicherbereich in den die Daten gelesen werden
                byte[] buffer = new byte[1024];
                int bytesRead;
                // Liest bis zu 1024 Bytes aus der Vorlage und speichert sie im buffer
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    // Schreibt gelesene Bytes in die Zieldatei
                    outputStream.write(buffer, 0, bytesRead);
                }
                // Erfolgsmeldung
                showPopup("The template was successfully downloaded: " + targetFile.getAbsolutePath(), true);
            } catch (IOException e) {
                // Fehlermeldung im Falle einer Ausnahme inkl. Fehlermeldung in der Konsole
                e.printStackTrace();
                showPopup("The template could not be downloaded.", false);
            }
        }
        // Nach Abschluss des Downloads wird der Dialog erneut automatisch angezeigt für Benutzerfreundlichkeit
        showLearnModeDialog();
    }

    private void startLearnMode() {
        // Überprüft ob ein File hochgeladen wurde
        if (uploadedCsvFile != null) {
            try {
                // Erstellt FXML Klasse um das learnmode_layout zu laden
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/learnmode_layout.fxml"));
                // Lädt die FXML Datei und erstellt den Wurzelknoten
                Parent lernmodusRoot = loader.load();

                // Ruft die Instanz des Controllers ab mit der die geladene FXML-Datei verknüpft ist
                LearnModeController learnModeController = loader.getController();
                // Ruft die Instanz des Controllers mit der Methode um die Fragen des CSV files zu lesen und übergibt
                // das hochgeladene File als Argument
                learnModeController.loadQuestionsFromCsv(uploadedCsvFile);

                // Prüft ab ob das hochgeladene File leer ist und verhindert das starten des Lernmodus
                if (learnModeController.getQuestions().isEmpty()) {
                    showPopup("The CSV-File has no valid data.", true);
                    return;
                }

                // Ruft die Szene ab die den Button enthält und wandelt das allgemeine Window in eine Stage um
                Stage stage = (Stage) learnModeButton.getScene().getWindow();
                // Wechselt die aktuelle Szene zu der neuen Szene
                stage.setScene(new Scene(lernmodusRoot));
                // Abschließend wird die Szene angezeigt
                stage.show();
            } catch (IOException e) {
                // Errorhandling im Ausnahmefall inkl. Fehlerausgabe in der Konsole
                e.printStackTrace();
                showPopup("A problem occurred while loading the Learn mode.", true);
            }
        } else {
            // Errorhandling im Ausnahmefall
            showPopup("No CSV-File available. Please upload a file first.", true);
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

    // Methode zum anzeigen verschiedener PopUp´s
    public void showPopup(String message, boolean showExitButton) {
        try {
            // FXML Objekt wird erstellt um das PopUp Layout zu laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/popups.fxml"));
            // Ladet FXML-Layout in ein Parent-Objekt die als Wurzel für eine Szene dient
            Parent popupRoot = loader.load();

            // Ruft PopUp Controller auf
            PopupController popupController = loader.getController();
            // Übergibt Nachricht an den Controller
            popupController.setPopupMessage(message);

            popupController.exitButton.setVisible(false);

            // Erstellt neue Stage für separates Fenster
            Stage popupStage = new Stage();
            // Erstellt neue Szene mit dem geladenen Layout als Wurzel
            popupStage.setScene(new Scene(popupRoot));
            // Setzt Titel
            popupStage.setTitle("Message");
            // Blockiert andere Fenster, solange das Popup aktiv ist
            popupStage.initModality(Modality.APPLICATION_MODAL);
            // Übergibt der PopUpController-Instanz die Stage Referenz
            // ermöglicht schließung der Stage indem User zb auf "Close" klickt
            popupController.setPopupStage(popupStage);
            // Wartet, bis der Benutzer das Popup schließt
            popupStage.showAndWait();
        } catch (IOException e) {
            // Gibt vollständige Fehlermeldung in der Konsole aus
            e.printStackTrace();
        }
    }

    @FXML
    private void onHighscoreClick(ActionEvent event) {
        try {
            // FXMLLoader läd FXML Datei (highscore.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/highscore_layout.fxml"));
            // liest Datei + gibt zurück den Root Node vom Layout (Start von Scene)
            Parent root = loader.load();

            // loader.getController() = erhälts controller instance => erstellt + initialisiert von FXMLLoader
            HighscoreController highscoreController = loader.getController();

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MICE MODE

    public void setMrSqueak() {
        mrSqueakLabel.setVisible(true);
        mrSqueakButton.setDisable(true);
    }

    @FXML
    private void onMiceModeClick(ActionEvent event) {

        try {
            // FXMLLoader läd FXML Datei (highscore.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/micemode_layout.fxml"));
            // liest Datei + gibt zurück den Root Node vom Layout (Start von Scene)
            Parent root = loader.load();

            // loader.getController() = erhälts controller instance => erstellt + initialisiert von FXMLLoader
            MiceModeController miceModeController = loader.getController();

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
