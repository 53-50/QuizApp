package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PopupController {

    // Exit Button damit man beim schließen nachfragt bist du sicher und abbrechen kann
    // muss public sein weil er woanders aufgegriffen wird
    public Button exitButton;

    @FXML
    private Label popupMessage;
    @FXML
    private VBox popup;

    private Stage popupStage;

    // damit man weiß ob beim Schließen Button wirklich geschlossen werden soll oder nicht
    // wird erst true wenn "OK" geklickt wurde und somit handleClosePopup() ausgelöst wird
    // erst dort wird userSure auf true gesetzt - davor kann nie Scene gewechselt werden
    // weil in QuizBase der Scneneswitch von userSure abhängt (getUserSure)
    private boolean userSure = false;

    // die wird dann gesetet wo es aufgerufen wird da man dann die spezifschie
    // bei handlenotcloseup braucht damit dieses popup abgebrochen wird
    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    // logik für schließen vom Pop-Up
    public void closePopup() {
        Stage stage = (Stage) popup.getScene().getWindow();
        stage.close();// Schließt das Pop-up
    }

    @FXML
    // wird ausgelöst wenn OK gedrückt wird
    public void handleClosePopup() {
        userSure = true;
        closePopup(); //erst wenn userSure also "OK" dann wird Popup geschlossen
    }

    // Methode, um die Nachricht im Pop-up zu setzen
    public void setPopupMessage(String message) {
        popupMessage.setText(message);  // Nachricht im Label ändern
    }

    // Gibt zurück, ob der Benutzer die Aktion bestätigt hat
    // wird gebraucht im QuizBase
    public boolean getUserSure() {
        return userSure;
    }

    @FXML
    // damit man bei "Bist du sicher?" abbrechen kann und das spiel weiterspielen kann
    // ausgelöst wenn "Back to Game" gedrückt wird
    public void handleNotClosePopup() {
        userSure = false;
        popupStage.close();
    }
}
