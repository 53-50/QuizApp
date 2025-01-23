package com.example.Controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

//Öffentliche Klasse Highscore für Verwaltung der Einträge + JavaFX-Controller-Logik
public class HighscoreController {

    /* ------------------------------------------- FXML-BINDINGS ------------------------------------------- */

    // Zeile in der Spieler sein erreichter Rang angezeigt wird
    @FXML
    private Label lblCurrentRank;

    // Komplette Tabelle
    @FXML
    private TableView<HighscoreEntry> tableHighscore;

    //Spalte Rang
    @FXML
    private TableColumn<HighscoreEntry, Number> colIndex;

    //Spalte Name
    @FXML
    private TableColumn<HighscoreEntry, String> colName;

    //Spalte Score
    @FXML
    private TableColumn<HighscoreEntry, Number> colScore;

    //Spalte Schwierigkeit
    @FXML
    private TableColumn<HighscoreEntry, String> colDifficulty;

    //Filter Button
    @FXML
    private MenuButton HighscoreFilterButton;

    //Auswahlmöglichkeiten DropDown Filter Button: Easy, Medium, Hard
    @FXML
    private void onFilterEasy() {
        applyFilter("easy");
    }
    @FXML
    private void onFilterMedium() {
        applyFilter("medium");
    }
    @FXML
    private void onFilterHard() {
        applyFilter("hard");
    }

    //wird von JavaFX aufgerufen, wenn die Szene geladen und mit Controller verknüpft wird
    @FXML
    public void initialize() {

        loadFromFile();

        // Wandelt Highscore Liste in ObservableList um und weist sie TableView zu
        ObservableList<HighscoreEntry> data = FXCollections.observableArrayList(highscoreList);
        tableHighscore.setItems(data);

        // Rang
        colIndex.setCellValueFactory(cellData -> {
            // Index = Position + 1
            HighscoreEntry entry = cellData.getValue();
            int idx = tableHighscore.getItems().indexOf(entry) + 1;
            return new SimpleObjectProperty<>(idx);
        });

        // Name
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPlayerName())
        );

        // Score
        colScore.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getScore())
        );

        // Difficulty (Mode)
        colDifficulty.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDifficulty())
        );


        // lastAddedRank wird über der Tabelle ausgegeben > Name + Rank Nummer
        if (lastAddedRank > 0 && lastAddedPlayerName != null) {
            lblCurrentRank.setText(lastAddedPlayerName + ", you are currently on rank # " + lastAddedRank + "!");

        } else {
            lblCurrentRank.setText("");
        }
    }


    /* ------------------------------------------- VARIABLEN ------------------------------------------- */

    // Liste aller Einträge, die in den Highscore aufgenommen wurden (Array Liste, die leer ist)
    private static List<HighscoreEntry> highscoreList = new ArrayList<>();

    // Ort zum Speichern und Laden der Datei > in Ordner Data/Highscore
    private static final Path HIGHSCORE_FILE = Paths.get("data", "highscore_data.csv");

    // Rang des zuletzt hinzugefügten Scores (-1 bedeutet noch keiner hinzugefügt)
    private static int lastAddedRank = -1;

    //Name des zuletzt hinzugefügten Spielers
    private static String lastAddedPlayerName = null;

    /* ------------------------------------------- EIGENE DATENKLASSE ------------------------------------------- */

    //innere statische Datenklasse, die jeweils einen Eintrag repräsentiert (Name,Score,Difficulty)
    public static class HighscoreEntry {
        private final String playerName;
        private final int score;
        private final String difficulty;


        public HighscoreEntry(String playerName, int score, String difficulty) {
            this.playerName = playerName;
            this.score = score;
            this.difficulty = difficulty;
        }

    /* -------------------------------------------------- GETTER ------------------------------------------------- */

        //Getter-Methoden, die Wert zurück geben
        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public String getDifficulty() {
            return difficulty;
        }
    }

    /* -------------------------------------------- VERWALTUNG EINTRÄGE ------------------------------------------- */


    //Methode, um in WinLose Highscore-Einträge hinzuzufügen zu können
    public static void addScore(String playerName, int finalScore, String difficulty) {

        loadFromFile();
        //Erstellt neues Highscore-Entry Objekt und legt es in Highscore Liste ab
        HighscoreEntry entry = new HighscoreEntry(playerName, finalScore, difficulty);
        highscoreList.add(entry);

        // Sortiert die Liste nach erziehlten Punkten (reversed = score absteigend)
        highscoreList.sort(Comparator.comparingInt(HighscoreEntry::getScore).reversed());

        // Ermittelt Rang des neuen Eintrags in sortierter Liste und speichert in lastAddedRank
        // Da Liste bei 0 startet Index + 1 = menschlich lesbarer Rang
        lastAddedRank = highscoreList.indexOf(entry) + 1;

        // Neuen Spielernamen merken
        lastAddedPlayerName = playerName;

        // Speichert aktualisierte Liste in CSV-Datei (damit persistent)
        saveToFile();
    }

    // Update des Punktestands bei Retry und ggf. in Highscore überschreiben
    public static void updateScoreIfBetter(String playerName, int newScore, String difficulty) {

        // Suche nach vorhandenem Eintrag mit gleichem Namen und Schwierigkeit
        HighscoreEntry existingEntry = null;
        for (HighscoreEntry entry : highscoreList) {
            if (entry.getPlayerName().equals(playerName)
                    && entry.getDifficulty().equalsIgnoreCase(difficulty)) {
                existingEntry = entry;
                break;
            }
        }

        if (existingEntry != null) {

            // Falls der neue Score höher ist: alten Eintrag entfernen und neuen hinzufügen
            if (newScore > existingEntry.getScore()) {
                highscoreList.remove(existingEntry);
                addScore(playerName, newScore, difficulty);
            }
            // Sonst nichts tun, wenn der alte Score schon höher oder gleich hoch ist

        } else {
            // Kein Eintrag vorhanden => neuen Score hinzufügen
            addScore(playerName, newScore, difficulty);
        }
    }


    // Highscore Liste laden aus Datei > Ordner: Data/Highscore
    private static void loadFromFile() {
        try {

            // Datei öffnen und Zeilen einlesen
            try (BufferedReader br = Files.newBufferedReader(HIGHSCORE_FILE)) {
                List<HighscoreEntry> loaded = new ArrayList<>();
                String line;

                //Jede Zeile wird in playername, Score und Diff aufgeteilt und in loaded gespeichert
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        String name = parts[0];
                        int sc = Integer.parseInt(parts[1]);
                        String diff = parts[2];
                        loaded.add(new HighscoreEntry(name, sc, diff));
                    }
                }
                // Liste sortieren
                loaded.sort(Comparator.comparingInt(HighscoreEntry::getScore).reversed());
                highscoreList = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Speichern in Datei > Ordner: Data/Highscore
    private static void saveToFile() {
        try {
            // Datei beschreiben
            try (BufferedWriter bw = Files.newBufferedWriter(HIGHSCORE_FILE)) {
                for (HighscoreEntry entry : highscoreList) {
                    bw.write(entry.getPlayerName() + ";" + entry.getScore() + ";" + entry.getDifficulty());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* ------------------------------------------- FILTER-FUNKTION ------------------------------------------- */

    //Methode zum Filter setzen durch User
    private void applyFilter(String diff) {

        //Zeigt als Text im Button statt "Filter" die jeweilige gefilterte Kategorie an
        HighscoreFilterButton.setText(diff.substring(0,1).toUpperCase()
                + diff.substring(1).toLowerCase());

        // Filtere nach passendem Schwierigkeitsgrad
        List<HighscoreEntry> filtered = highscoreList.stream()
                .filter(e -> e.getDifficulty().equalsIgnoreCase(diff))
                .collect(Collectors.toList());

        // Tabelle aktualisieren
        tableHighscore.setItems(FXCollections.observableArrayList(filtered));

        // Berechnet den neuen Rang des zuletzt hinzugefügten Spielers in der gefilterten Liste
        int filteredRank = -1;
        for (int i = 0; i < filtered.size(); i++) {
            if (filtered.get(i).getPlayerName().equals(lastAddedPlayerName)) {
                filteredRank = i + 1; // Index + 1, um den Rang anzuzeigen
                break;
            }
        }

        // Aktualisiert das Label über der Tabelle
        if (filteredRank > 0 && lastAddedPlayerName != null) {
            lblCurrentRank.setText(lastAddedPlayerName + ", your rank in this difficulty is #" +
                    filteredRank + "!");
        } else {
            lblCurrentRank.setText("");
        }

        // Difficulty-Spalte ausblenden, da nach einer Schwierigkeit gefiltert (daher nicht notwendig)
        tableHighscore.getColumns().remove(colDifficulty);

    }

    // Button zum Filter wieder löschen
    @FXML
    private void onClearFilter() {

        //gesamte Highscore Liste wird wieder angezeigt
        tableHighscore.setItems(FXCollections.observableArrayList(highscoreList));

        // Zeigt den Rang des zuletzt hinzugefügten Spielers wieder in der gesamten Liste an
        if (lastAddedRank > 0 && lastAddedPlayerName != null) {
            lblCurrentRank.setText(lastAddedPlayerName + ", you are currently on rank #" + lastAddedRank + "!");
        } else {
            lblCurrentRank.setText("");
        }

        // Difficulty-Spalte wieder anzeigen
        if (!tableHighscore.getColumns().contains(colDifficulty)) {
            tableHighscore.getColumns().add(colDifficulty);
        }

        //Zeigt wieder "Filter" als Text im Button an
        HighscoreFilterButton.setText("Filter");

    }


    /* ------------------------------------------- SZENENWECHSEL ------------------------------------------- */

    //Zurück ins Main Menü
    @FXML
    private void onBackClick(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layouts/main_menu.fxml"));
            Parent root = loader.load();
            Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            st.setScene(new Scene(root));
            st.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}




