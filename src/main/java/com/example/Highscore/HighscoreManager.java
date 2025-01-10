package com.example.Highscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighscoreManager {


    // Privater Konstruktor nur zum Abrufen in anderen Klassen
    private HighscoreManager() {
    }

    //static instance sichert, dass nur eine Instanz erstellt werden kann
    private static final HighscoreManager instance = new HighscoreManager();
    public static HighscoreManager getInstance() { //Singleton-Pattern, damit es nur einen Highscore Manager gibt
        return instance;
    }

    //Speicherung aller Highscore Einträge
    private List<HighscoreEntry> highscoreList = new ArrayList<>();

   //Methode, um neuen Score in Highscore Liste einzutragen
    public void addScore(String playerName, int score, String difficulty) {

        // Neues Highscore-Objekt zur Kapselung von Name, Punkte, Modus
        HighscoreEntry entry = new HighscoreEntry(playerName, score, difficulty);
        //Speicherung des neuen Eintrags in die Highscore Array Liste (unsortiert)
        highscoreList.add(entry);

        // Sort-Methode um Liste zu sortieren: absteigend nach Score
        highscoreList.sort(Comparator.comparingInt(HighscoreEntry::getScore).reversed());
        //Comparator.comparingInt(HighscoreEntry::getScore) erzeugt Comparator, der nach getScore aufsteigend sortiert (anhand int Ganzzahl Ergebnissen)
        //.reversed kehrt die Liste um -> auf absteigend
        //highscoreList.sort(..) sortiert Liste mit Comparator


        //Begrenzung auf maximal 20 Einträge
        if (highscoreList.size() > 20) { //wenn Liste über 20
            highscoreList = highscoreList.subList(0, 20); //erstellt Sicht auf die ersten 20 Elemente der Liste
        }

    }

    //Getter für die gesamte Highscore Liste
    public List<HighscoreEntry> getHighscoreList() {
        return highscoreList;
    }
}

