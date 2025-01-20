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

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class HighscoreController {

    /* -------------------- EIGENE DATENKLASSE -------------------- */

    public static class HighscoreEntry {
        private final String playerName;
        private final int score;
        private final String difficulty;


        public HighscoreEntry(String playerName, int score, String difficulty) {
            this.playerName = playerName;
            this.score = score;
            this.difficulty = difficulty;
        }

        //----GETTER----//

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

    /* -------------------- VERWALTUNG EINTRÄGE -------------------- */

    // Liste aller Einträge
    private static List<HighscoreEntry> highscoreList = new ArrayList<>();

    // Ort zum Speichern und Laden der Datei (CSV)
    private static final Path HIGHSCORE_FILE = Paths.get("data", "highscore_data.csv");

    // Rang des zuletzt hinzugefügten Scores
    private static int lastAddedRank = -1;

    private static String lastAddedPlayerName = null;

    // Daten beim allerersten Zugriff laden
    static {
        loadFromFile();
    }

    //Methode, um in WinLose Highscore-Einträge hinzuzufügen
    public static void addScore(String playerName, int score, String difficulty) {
        HighscoreEntry entry = new HighscoreEntry(playerName, score, difficulty);
        highscoreList.add(entry);

        // Sortieren (score absteigend)
        highscoreList.sort(Comparator.comparingInt(HighscoreEntry::getScore).reversed());

        // Rang (= Index + 1)
        lastAddedRank = highscoreList.indexOf(entry) + 1;

        // Neuen Spielernamen merken:
        lastAddedPlayerName = playerName;

        // Speichern
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


    // Laden aus CSV-Datei
    private static void loadFromFile() {
        try {
            // 1) Ordner anlegen, falls er nicht existiert
            if (Files.notExists(HIGHSCORE_FILE.getParent())) {
                Files.createDirectories(HIGHSCORE_FILE.getParent());
            }

            // 2) Falls die Datei noch nicht existiert, gibt es einfach keine Highscore-Daten
            if (Files.notExists(HIGHSCORE_FILE)) {
                return;  // => leere Liste
            }

            // 3) Datei öffnen und Zeilen einlesen
            try (BufferedReader br = Files.newBufferedReader(HIGHSCORE_FILE)) {
                List<HighscoreEntry> loaded = new ArrayList<>();
                String line;
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

    // Speichern in CSV
    private static void saveToFile() {
        try {
            // 1) Ordner erzeugen, falls nötig
            if (Files.notExists(HIGHSCORE_FILE.getParent())) {
                Files.createDirectories(HIGHSCORE_FILE.getParent());
            }

            // 2) Datei nicht vorhanden? -> evtl. anlegen (optional)
            if (Files.notExists(HIGHSCORE_FILE)) {
                Files.createFile(HIGHSCORE_FILE);
            }

            // 3) Datei beschreiben
            try (BufferedWriter bw = Files.newBufferedWriter(HIGHSCORE_FILE)) {
                for (HighscoreEntry e : highscoreList) {
                    bw.write(e.getPlayerName() + ";" + e.getScore() + ";" + e.getDifficulty());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* -------------------- FXML-BINDINGS -------------------- */

    // Zeigt den Range an
    @FXML
    private Label lblCurrentRank;

    // Index-Spalte (Platz)
    @FXML
    private TableView<HighscoreEntry> tableHighscore;

    @FXML
    private MenuButton HighscoreFilterButton;

    @FXML
    private TableColumn<HighscoreEntry, Number> colIndex;

    @FXML
    private TableColumn<HighscoreEntry, String> colName;

    @FXML
    private TableColumn<HighscoreEntry, Number> colScore;

    @FXML
    private TableColumn<HighscoreEntry, String> colDifficulty;


    @FXML
    public void initialize() {


        // ObservableList erstellen
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

        // lastAddedRank
        if (lastAddedRank > 0 && lastAddedPlayerName != null) {
            lblCurrentRank.setText(lastAddedPlayerName + ", you are currently on rank # " + lastAddedRank + "!");
        } else {
            lblCurrentRank.setText("");
        }
    }


    /* -------------------- FILTER-FUNKTION -------------------- */

    private void applyFilter(String diff) {

        HighscoreFilterButton.setText(diff.substring(0,1).toUpperCase() + diff.substring(1).toLowerCase());

        // Filtere nach passendem Schwierigkeitsgrad
        List<HighscoreEntry> filtered = highscoreList.stream()
                .filter(e -> e.getDifficulty().equalsIgnoreCase(diff))
                .collect(Collectors.toList());

        // Tabelle aktualisieren
        tableHighscore.setItems(FXCollections.observableArrayList(filtered));

        // Difficulty-Spalte ausblenden
        tableHighscore.getColumns().remove(colDifficulty);

    }


    @FXML
    private void onFilterEasy(ActionEvent e) {
        applyFilter("easy");
    }
    @FXML
    private void onFilterMedium(ActionEvent e) {
        applyFilter("medium");
    }
    @FXML
    private void onFilterHard(ActionEvent e) {
        applyFilter("hard");
    }

    @FXML
    private void onClearFilter(ActionEvent e) {
        tableHighscore.setItems(FXCollections.observableArrayList(highscoreList));

        // Difficulty-Spalte wieder anzeigen
        if (!tableHighscore.getColumns().contains(colDifficulty)) {
            tableHighscore.getColumns().add(colDifficulty);
        }

        HighscoreFilterButton.setText("Filter");

    }


    /* -------------------- SZENENWECHSEL -------------------- */

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




