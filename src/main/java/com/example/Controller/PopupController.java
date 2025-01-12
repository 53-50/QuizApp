package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PopupController {

    // Exit Button damit man beim schließen nachfragt bist du sicher und abbrechen kann
    public Button exitButton;
    @FXML
    private Label popupMessage;

    @FXML
    private VBox popup;

    private Stage popupStage;

    // damit man weiß ob beim Schließen Button wirklich geschlossen werden soll oder nicht
    private boolean userSure = false;

    // die wird dann gesetet wo es aufgerufen wird da man dann die spezifschie
    // bei handlenotcloseup braucht damit dieses popup abgebrochen wird
    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    @FXML
    public void closePopup() {
        Stage stage = (Stage) popup.getScene().getWindow();
        stage.close();// Schließt das Pop-up-Fenster
    }

    // wird ausgelöst wenn OK gedrückt wird
    public void handleClosePopup() {
        userSure = true;
        closePopup(); //erst wenn userSure dann wird Popup geschlossen
    }

    // Methode, um die Nachricht im Pop-up zu setzen
    public void setPopupMessage(String message) {
        popupMessage.setText(message);  // Nachricht im Label ändern
    }

    // Gibt zurück, ob der Benutzer die Aktion bestätigt hat
    public boolean isUserSure() {
        return userSure;
    }

    // damit man bei "Bist du sicher?" abbrechen kann und das spiel weiterspielen kann
    public void handleNotClosePopup() {
        userSure = false;
        popupStage.close();
    }
}
