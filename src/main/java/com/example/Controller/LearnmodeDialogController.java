package com.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class LearnmodeDialogController {
    // Definiert Variable für Beschreibungstext
    @FXML
    private Label description;
    // Initalisiert Berschreibungstext
    @FXML
    public void initialize() {
        description.setText("""
            Welcome to the Learning Mode!

            Please download a template if you don't have one yet and enter your questions in the column <Questions> and your answers in the column <Answers>.

            Finally upload the filled-out template. The Learning Mode will start automatically after the upload.

            Happy learning :)
            """);
    }

    // Interfacefeld zum speichern einer Instanz - Aktionen im Dialog werden an Hauptklasse weitergeleitet
    private LearnmodeDialogListener listener;

    // Stellt Listener für Hauptklasse bereit wenn User eine Aktion ausführt
    public void setListener (LearnmodeDialogListener listener) {
        this.listener = listener;
    }

    // Methode für die Verknüpfung #onUploadClick
    @FXML
    private void onUploadClick (ActionEvent event) {
        if (listener != null) {
            // Benachrichtigt Listner dass der Benutzer "Upload" ausgewählt hat
            listener.onUploadSelected();
        }
        closeDialog(event);
    }

    // Methode für die Verknüpfung #onDownloadTemplateClick
    @FXML
    private void onDownloadTemplateClick (ActionEvent event) {
        if (listener != null) {
            // Benachrichtigt Listner dass der Benutzer "Download Template" ausgewählt hat
            listener.onDownloadTemplateSelected();
        }
        closeDialog(event);
    }

    // Methode für die Verknüpfung #onCancelClick
    @FXML
    private void onCancelClick (ActionEvent event) {
        // Schließt Dialog ohne Listener Benachrichtigung
        closeDialog(event);
    }

    // Methode zum schließen des Fensters nachdem eine Aktion gewählt wurde
    private void closeDialog (ActionEvent event) {
        // Ruft die Stage des Fensters ab in dem der Button geklickt wurde
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public interface LearnmodeDialogListener {
        // Wird aufgerufen wenn der Benutzer "Upload" klickt
        void onUploadSelected();
        // Wird aufgerufen wenn der Benutzer "Downlod Template" klickt
        void onDownloadTemplateSelected();
    }
}
