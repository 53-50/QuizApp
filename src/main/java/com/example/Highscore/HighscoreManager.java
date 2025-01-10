package com.example.Highscore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighscoreManager {

    // Singleton oder statisch
    private static HighscoreManager instance = new HighscoreManager();
    public static HighscoreManager getInstance() {
        return instance;
    }

    private HighscoreManager() {
        // ggf. aus Datei laden
    }

    private List<HighscoreEntry> highscoreList = new ArrayList<>();

    public void addScore(String playerName, int score, String difficulty) {
        // Neues Highscore-Objekt
        HighscoreEntry entry = new HighscoreEntry(playerName, score, difficulty);
        highscoreList.add(entry);

        // Liste sortieren: absteigend nach Score
        highscoreList.sort(Comparator.comparingInt(HighscoreEntry::getScore).reversed());

        // Nur Top 20 behalten
        if (highscoreList.size() > 20) {
            highscoreList = highscoreList.subList(0, 20);
        }

        // optional: speichere in einer Datei oder Datenbank
    }

    public List<HighscoreEntry> getHighscoreList() {
        return highscoreList;
    }
}

