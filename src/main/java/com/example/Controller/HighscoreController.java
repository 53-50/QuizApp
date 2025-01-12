package com.example.Controller;
import com.example.Highscore.HighscoreEntry;
import com.example.Highscore.HighscoreManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HighscoreController {

    @FXML
    private Label Highscore;

    @FXML
    private TableView<HighscoreEntry> tableHighscore;

    // Unterspalten für LEICHT
    @FXML
    private TableColumn<HighscoreEntry, String> colLeichtPlayer;
    @FXML
    private TableColumn<HighscoreEntry, Number> colLeichtScore;

    // Unterspalten für MITTEL
    @FXML
    private TableColumn<HighscoreEntry, String> colMittelPlayer;
    @FXML
    private TableColumn<HighscoreEntry, Number> colMittelScore;

    // Unterspalten für SCHWER
    @FXML
    private TableColumn<HighscoreEntry, String> colSchwerPlayer;
    @FXML
    private TableColumn<HighscoreEntry, Number> colSchwerScore;


    @FXML
    public void initialize() {
        //Spalte wird an Fenstergröße angepasst
        tableHighscore.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Highscore-Liste aus dem Manager laden
        List<HighscoreEntry> highscores = HighscoreManager.getInstance().getHighscoreList();
        ObservableList<HighscoreEntry> data = FXCollections.observableArrayList(highscores);

        //TableView mit Daten füllen
        tableHighscore.setItems(data);

        //CellValueFactorys setzen:
        //Damit jede Zeile in den Spalten nur dann Text anzeigt, wenn die Difficulty stimmt

        colLeichtPlayer.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("Leicht".equalsIgnoreCase(entry.getDifficulty())) {
                // Zeige den PlayerName an
                return new SimpleStringProperty(entry.getPlayerName());
            } else {
                // Anderenfalls leer
                return new SimpleStringProperty("");
            }
        });

        colLeichtScore.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("Leicht".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleObjectProperty<>(entry.getScore());
            } else {
                return new SimpleObjectProperty<>(0);
            }
        });

        colMittelPlayer.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("Mittel".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleStringProperty(entry.getPlayerName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        colMittelScore.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("Mittel".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleObjectProperty<>(entry.getScore());
            } else {
                return new SimpleObjectProperty<>(0);
            }
        });

        colSchwerPlayer.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("Schwer".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleStringProperty(entry.getPlayerName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        colSchwerScore.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("Schwer".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleObjectProperty<>(entry.getScore());
            } else {
                return new SimpleObjectProperty<>(0);
            }
        });
    }

    // Zeigt nur Einträge mit passender Difficulty in der Tabelle an
    private void applyFilter(String modus) {
        List<HighscoreEntry> allEntries = HighscoreManager.getInstance().getHighscoreList();
        List<HighscoreEntry> filtered = allEntries.stream()
                .filter(e -> e.getDifficulty().equalsIgnoreCase(modus))
                .collect(Collectors.toList());
        tableHighscore.setItems(FXCollections.observableArrayList(filtered));

        //Spalten-Sichtbarkeit steuern
        switch (modus) {
            case "Leicht":
                colLeichtPlayer.setVisible(true);
                colLeichtScore.setVisible(true);

                colMittelPlayer.setVisible(false);
                colMittelScore.setVisible(false);
                colSchwerPlayer.setVisible(false);
                colSchwerScore.setVisible(false);
                break;

            case "Mittel":
                colLeichtPlayer.setVisible(false);
                colLeichtScore.setVisible(false);

                colMittelPlayer.setVisible(true);
                colMittelScore.setVisible(true);
                colSchwerPlayer.setVisible(false);
                colSchwerScore.setVisible(false);
                break;

            case "Schwer":
                colLeichtPlayer.setVisible(false);
                colLeichtScore.setVisible(false);
                colMittelPlayer.setVisible(false);
                colMittelScore.setVisible(false);

                colSchwerPlayer.setVisible(true);
                colSchwerScore.setVisible(true);
                break;
        }
    }

    @FXML
    private void onFilterLeicht(ActionEvent event) {
        applyFilter("Leicht");
    }

    @FXML
    private void onFilterMittel(ActionEvent event) {
        applyFilter("Mittel");
    }

    @FXML
    private void onFilterSchwer(ActionEvent event) {
        applyFilter("Schwer");
    }

    // Button "Zurück zum Menü"
    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        switchScene(actionEvent, "/Layouts/main_menu.fxml");
    }

    // Hilfsmethode zum Szenenwechsel, ähnlich wie in WinLoseLayoutController
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    //Methode, um den Filter wieder zu löschen und alle Spalten + Daten sichtbar zu machen
    private void onClearFilter(ActionEvent event) {
        // Alle Highscore-Einträge holen
        List<HighscoreEntry> allEntries = HighscoreManager.getInstance().getHighscoreList();
        tableHighscore.setItems(FXCollections.observableArrayList(allEntries));

        // Alle Spalten wieder sichtbar machen, falls man Spalten ausblendet
        colLeichtPlayer.setVisible(true);
        colLeichtScore.setVisible(true);
        colMittelPlayer.setVisible(true);
        colMittelScore.setVisible(true);
        colSchwerPlayer.setVisible(true);
        colSchwerScore.setVisible(true);


    }

}


