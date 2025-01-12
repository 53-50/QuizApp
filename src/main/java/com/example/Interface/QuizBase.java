package com.example.Interface;

import com.example.Controller.PopupController;
import com.example.Controller.WinLoseLayoutController;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public interface QuizBase {


    default void markQuestionAsRight(int punkte, int rightOnes, Label punktetext) {
        // Logik zum Markieren der aktuellen Frage als falsch
        punktetext.setText(Integer.toString(punkte));
    }

    default void markQuestionAsWrong(int leben, Label lebentext) {
        // Logik zum Markieren der aktuellen Frage als falsch
        lebentext.setText(Integer.toString(leben));
    }

    // richtig und falsch buttons anzeigen
    // muss selbst implementiert werden, weil richtige fragenlogik unterschiedlich
    void setAnswerButtonColors();

    default void resetAnswerButtonColors(Button antwort1, Button antwort2, Button antwort3, Button antwort4) {
        // Alle Buttons wieder auf die Standardfarbe setzen
        Button[] answerButtons = {antwort1, antwort2, antwort3, antwort4};
        for (Button button : answerButtons) {
            button.setStyle("-fx-text-fill: black;"); // Standardfarbe zurücksetzen
        }
    }

    //allgemeine wenn auf das schließen button geklickt wird
    default void onExit(ActionEvent event, Timeline timer) {

        // braucht man damit popup auftaucht und er nach bist du sicher das spiel beendet wird
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/popups.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupMessage("Bist du sicher?");

            // Nachricht setzen
            popupController.exitButton.setVisible(true);

            Stage popupStage = new Stage();
            popupStage.setTitle("Exit Pop-up");
            popupStage.setScene(new Scene(root));

            // Stage an den Controller übergeben, damit der Button es später schließen kann
            popupController.setPopupStage(popupStage);

            // Modus von Stage festzulegen
            // Blockiert alle anderen Fenster der Anwendung
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Aussehen und Verhalten von Stage ändern
            // keine Titelleiste + nicht verschiebbar od. größen verändertnd
            popupStage.initStyle(StageStyle.UNDECORATED);

            if (timer != null) {
                timer.pause();  // Pausiert den Timer
            }

            // Zeigt das Pop-up an und wartet, bis es geschlossen wird
            popupStage.initModality(Modality.APPLICATION_MODAL);  // Macht das Pop-up modal (blockiert das Hauptfenster)
            popupStage.showAndWait();

            if(popupController.isUserSure()) {
                // Wenn das Pop-up geschlossen wird, führe das aus:
                switchScene(event, "/Layouts/main_menu.fxml");
            } else {
                timer.play();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //allgemeinen Scenen switchen festlegen
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {

            //sich neues FXML holen
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Aktuelle Stage holen und neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //showendscreen
    default void showEndScreen(Stage stage, String playerName, ControllerBase controller) {
        try {
            // Lade den Win Screen (FXML-Datei)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/win_lose_layout.fxml"));
            Parent winRoot = loader.load();

            // WinScreenController holen
            WinLoseLayoutController winLoseLayoutControllerController = loader.getController();

            // Referenz zum aktuellen QuizController und den Playernamen übergeben
            winLoseLayoutControllerController.setQuizController(controller);
            winLoseLayoutControllerController.setPlayerName(playerName);

            // Anzeige der Punktzahl aktualisieren
            winLoseLayoutControllerController.displayPunkte();
            winLoseLayoutControllerController.displayLeben();
            winLoseLayoutControllerController.displayWinOrLose();
            winLoseLayoutControllerController.displayRight();
            winLoseLayoutControllerController.displayNamenLabel();
            winLoseLayoutControllerController.displayModus();

            // Szene wechseln
            Scene winScene = new Scene(winRoot);
            stage.setScene(winScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
