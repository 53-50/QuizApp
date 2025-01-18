package com.example.Interface;

import com.example.Controller.PopupController;
import com.example.Controller.WinLoseController;

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

    // damit Namen Übergabe vom Main Menu möglich ist
    void setPlayerName(String name);

    // damit das Spiel initialisiert wird
    void initialize() throws IOException;

    // Timer wird gestartet
    void startTimer();

    // Timer wird zurückgesetzt
    void resetTimer();

    // was passiert wenn die Zeit abläuft wird hier gehandelt
    void handleTimeOut() throws IOException;

    // die aktuelle Frage wird angezeigt
    void displayCurrentQuestion() throws IOException, InterruptedException;

    // was passiert wenn ein Button als Antwort geklickt wurde
    void handleAnswerButtonClick(ActionEvent mainEvent) throws IOException, InterruptedException;

    // Feedback wird angezeigt je nach obs korrekt oder falsch ist
    void showFeedback(String feedback, boolean isCorrect);

    // es wird kontrolliert ob die angeklickte antwort die richtige antwort ist
    void checkAnswer(String givenAnswer);

    // was alles passieren soll wenn die frage richtig beantwortet wurde
    default void markQuestionAsRight(int points, int rightOnes, Label pointsLabel) {
        // Logik zum Markieren der aktuellen Frage als richtig
        pointsLabel.setText(Integer.toString(points));
    }

    // was alles passieren soll wenn die frage falsch beantwortet wurde
    default void markQuestionAsWrong(int lives, Label livesLabel) {
        // Logik zum Markieren der aktuellen Frage als falsch
        livesLabel.setText(Integer.toString(lives));
    }

    // richtig und falsch buttons graphisch anzeigen was die richtige/falsche antwort gewesen wäre
    void setAnswerButtonColors();

    // die buttoncolors zurücksetzten auf ursprünglichen look (black)
    default void resetAnswerButtonColors(Button answer1, Button answer2, Button answer3, Button answer4) {
        // Alle Buttons wieder auf die Standardfarbe setzen
        Button[] answerButtons = {answer1, answer2, answer3, answer4};
        for (Button button : answerButtons) {
            button.setStyle("-fx-text-fill: white;"); // Standardfarbe zurücksetzen
        }
    }

    //wenn auf den Exit button geklickt wird
    default void onExit(ActionEvent event, Timeline timer) {

        // braucht man damit popup auftaucht und erst nach "bist du sicher -> OK" das spiel beendet wird
        try {
            // FXML von Pop-Up wird geladen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/popups.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            // Pop-Up Message wird gesetzt
            popupController.setPopupMessage("Are you really sure?");

            // Der extra Button mit der Auswahl "Back to Game" wird hier angezeigt da er
            // bei anderen Pop-Ups nicht gebraucht wird
            popupController.exitButton.setVisible(true);

            Stage popupStage = new Stage();
            popupStage.setTitle("Exit Pop-up");
            popupStage.setScene(new Scene(root));

            // Stage an den Controller übergeben, damit der Button es später schließen kann
            popupController.setPopupStage(popupStage);

            // Modus von Stage festzulegen
            // Blockiert alle anderen Fenster der Anwendung - macht Pop-up modal
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Aussehen und Verhalten von Stage ändern
            // keine Titelleiste + nicht verschiebbar od. größen verändertnd
            popupStage.initStyle(StageStyle.UNDECORATED);

            if (timer != null) {
                timer.pause();  // Pausiert den Timer
            }

            // Zeigt das Pop-up an und wartet, bis es geschlossen wird
            popupStage.showAndWait();

            // nur wenn isUserSure() true dann wird die scene zu Main Menu gewechselt
            if(popupController.getUserSure()) {
                // Wenn das Pop-up geschlossen wird, führe das aus:
                switchScene(event, "/Layouts/main_menu.fxml");
            } else {
                // wenn nicht userSure also "Back to Game" gedrückt wurde - geht Game und Timer weiter
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

    //showendscreen - Win/Lose wird angezeigt und PlayerName + Controller wird übergeben
    default void showEndScreen(Stage stage, String playerName, ControllerBase controller) {
        try {
            // Lade den Win Screen (FXML-Datei)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/win_lose_layout.fxml"));
            Parent winRoot = loader.load();

            // WinScreenController holen
            WinLoseController winLoseLayoutControllerController = loader.getController();

            // Referenz zum aktuellen QuizController und den Playernamen übergeben
            winLoseLayoutControllerController.setQuizController(controller);
            winLoseLayoutControllerController.setPlayerName(playerName);

            // Anzeige der Statistik aktualisieren mit Methoden vom WinLoseLayoutController
            winLoseLayoutControllerController.displayPoints();
            winLoseLayoutControllerController.displayLives();
            winLoseLayoutControllerController.displayWinOrLose();
            winLoseLayoutControllerController.displayRight();
            winLoseLayoutControllerController.displayNameLabel();
            winLoseLayoutControllerController.displayMode();

            // Szene wechseln
            Scene winScene = new Scene(winRoot);
            stage.setScene(winScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
