package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PopupController {

    @FXML
    private Label popupMessage;

    @FXML
    private VBox popup;

    // damit man weiß ob beim Schließen Button wirklich geschlossen werden soll oder nicht
    private boolean userSure = false;

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
}
