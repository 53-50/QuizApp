package com.example.Controller;

import java.util.List;
import com.example.Highscore.HighscoreEntry;
import com.example.Highscore.HighscoreManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


    public class HighscoreController {

        public Label lblHighscore;

        @FXML
        private void onFilterChoice(ActionEvent event) {
            MenuItem menuItem = (MenuItem) event.getSource();
            String chosenMode = menuItem.getText(); //Leicht, Mittel, Schwer
            applyFilter(chosenMode);
        }

        //Methode Filter-Logik - zeigt im TableView nur Einträge des passenden Spielmodus an
        private void applyFilter(String modus) {
        }

        //Methode für den gesetzten Filter Leicht
        public void onFilterLeicht(ActionEvent actionEvent) {
        }

        //Methode für den gesetzten Filter Mittel
        public void onFilterMittel(ActionEvent actionEvent) {
        }

        //Methode für den gesetzten Filter Schwer
        public void onFilterSchwer(ActionEvent actionEvent) {
        }

        //Methode für Zurück zum Menue
        public void onBackClick(ActionEvent actionEvent) {
        }

        @FXML
        private TableView<HighscoreEntry> tableHighscore;
        @FXML
        private TableColumn<HighscoreEntry, String> colName;
        @FXML
        private TableColumn<HighscoreEntry, Integer> colScore;
        @FXML
        private TableColumn<HighscoreEntry, String> colDifficulty;

        @FXML
        public void initialize() {
            colName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
            colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
            colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

            // Daten aus dem Manager holen
            List<HighscoreEntry> highscores = HighscoreManager.getInstance().getHighscoreList();

            // ObservableList draus machen
            ObservableList<HighscoreEntry> data = FXCollections.observableArrayList(highscores);

            // In die Tabelle setzen
            tableHighscore.setItems(data);
        }
    }


