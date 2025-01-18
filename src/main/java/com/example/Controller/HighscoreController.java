package com.example.Controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
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
        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
        public String getDifficulty() { return difficulty; }
    }

    /* -------------------- VERWALTUNG DER EINTRÄGE -------------------- */

    // Liste aller Einträge
    private static List<HighscoreEntry> highscoreList = new ArrayList<>();

    // Ort zum Speichern und Laden der Datei (CSV)
    private static final String FILE_PATH = "highscore_data.csv";

    // Rang des zuletzt hinzugefügten Scores
    private static int lastAddedRank = -1;

    // Daten beim allerersten Zugriff laden
    static {
        loadFromFile();
    }

    //Methode, um in anderen Controllern Highscore-Einträge hinzuzufügen
    public static void addScore(String playerName, int score, String difficulty) {
        HighscoreEntry entry = new HighscoreEntry(playerName, score, difficulty);
        highscoreList.add(entry);

        // Sortieren (score absteigend)
        highscoreList.sort(Comparator.comparingInt(HighscoreEntry::getScore).reversed());
        // Auf 20 Einträge begrenzen
        if (highscoreList.size() > 20) {
            highscoreList = new ArrayList<>(highscoreList.subList(0, 20));
        }

        // Rang (= Index + 1)
        lastAddedRank = highscoreList.indexOf(entry) + 1;

        // Speichern
        saveToFile();
    }

    // Laden aus CSV-Datei
    private static void loadFromFile() {
        Path p = Paths.get(FILE_PATH);
        if (!Files.exists(p)) {
            return; // Keine Datei => leere Liste
        }
        List<HighscoreEntry> loaded = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(p)) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sortieren
        loaded.sort(Comparator.comparingInt(HighscoreEntry::getScore).reversed());
        highscoreList = loaded;
    }

    // Speichern in CSV
    private static void saveToFile() {
        Path p = Paths.get(FILE_PATH);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (HighscoreEntry e : highscoreList) {
                bw.write(e.getPlayerName() + ";" + e.getScore() + ";" + e.getDifficulty());
                bw.newLine();
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

    // Easy
    @FXML
    private TableColumn<HighscoreEntry, Number> colIndex;
    @FXML
    private TableColumn<HighscoreEntry, String> colEasyPlayer;
    @FXML
    private TableColumn<HighscoreEntry, Number> colEasyScore;

    // Medium
    @FXML
    private TableColumn<HighscoreEntry, String> colMediumPlayer;
    @FXML
    private TableColumn<HighscoreEntry, Number> colMediumScore;

    // Hard
    @FXML
    private TableColumn<HighscoreEntry, String> colHardPlayer;
    @FXML
    private TableColumn<HighscoreEntry, Number> colHardScore;

    @FXML
    public void initialize() {

        // Aktuelle Daten in die TableView packen
        // (ohne Filter => zeigt alle an)
        ObservableList<HighscoreEntry> data = FXCollections.observableArrayList(highscoreList);
        tableHighscore.setItems(data);

        // Index-Spalte = Zeilennummer bzw. Rang
        colIndex.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            int idx = tableHighscore.getItems().indexOf(entry) + 1;
            return new SimpleObjectProperty<>(idx);
        });

        // EASY-Spalten
        colEasyPlayer.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("easy".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleStringProperty(entry.getPlayerName());
            }
            return new SimpleStringProperty("");
        });
        colEasyScore.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("easy".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleObjectProperty<>(entry.getScore());
            }
            return new SimpleObjectProperty<>(0);
        });

        // MEDIUM-Spalten
        colMediumPlayer.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("medium".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleStringProperty(entry.getPlayerName());
            }
            return new SimpleStringProperty("");
        });
        colMediumScore.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("medium".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleObjectProperty<>(entry.getScore());
            }
            return new SimpleObjectProperty<>(0);
        });

        // HARD-Spalten
        colHardPlayer.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("hard".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleStringProperty(entry.getPlayerName());
            }
            return new SimpleStringProperty("");
        });
        colHardScore.setCellValueFactory(cellData -> {
            HighscoreEntry entry = cellData.getValue();
            if ("hard".equalsIgnoreCase(entry.getDifficulty())) {
                return new SimpleObjectProperty<>(entry.getScore());
            }
            return new SimpleObjectProperty<>(0);
        });

        // Falls zuletzt ein Eintrag hinzugefügt wurde:
        if (lastAddedRank > 0) {
            lblCurrentRank.setText("You are currently on rank #" + lastAddedRank + "!");
        } else {
            lblCurrentRank.setText("");
        }
    }

    /* -------------------- FILTER-FUNKTION -------------------- */

    private void applyFilter(String diff) {
        List<HighscoreEntry> filtered = highscoreList.stream()
                .filter(e -> e.getDifficulty().equalsIgnoreCase(diff))
                .collect(Collectors.toList());
        tableHighscore.setItems(FXCollections.observableArrayList(filtered));

        // Regeleung der Spalten-Sichtbarkeit nach Filter setzen
        switch (diff.toLowerCase()) {
            case "easy":
                colEasyPlayer.setVisible(true); //Easy wird angezeigt
                colEasyScore.setVisible(true);

                colMediumPlayer.setVisible(false);
                colMediumScore.setVisible(false);

                colHardPlayer.setVisible(false);
                colHardScore.setVisible(false);
                break;
            case "medium":
                colEasyPlayer.setVisible(false);
                colEasyScore.setVisible(false);

                colMediumPlayer.setVisible(true); //Medium wird angezeigt
                colMediumScore.setVisible(true);

                colHardPlayer.setVisible(false);
                colHardScore.setVisible(false);
                break;
            case "hard":
                colEasyPlayer.setVisible(false);
                colEasyScore.setVisible(false);

                colMediumPlayer.setVisible(false);
                colMediumScore.setVisible(false);

                colHardPlayer.setVisible(true); //Hard wird angezeigt
                colHardScore.setVisible(true);
                break;
            default:
                onClearFilter(null);
        }
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
        // Filter löschen und alle wieder anzeigen
        tableHighscore.setItems(FXCollections.observableArrayList(highscoreList));

        colEasyPlayer.setVisible(true);
        colEasyScore.setVisible(true);
        colMediumPlayer.setVisible(true);
        colMediumScore.setVisible(true);
        colHardPlayer.setVisible(true);
        colHardScore.setVisible(true);
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




