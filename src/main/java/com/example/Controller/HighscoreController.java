package com.example.Controller;

import java.util.ArrayList;
import java.util.List;

public class HighscoreController {

    //Interne Liste zur Speicherung der Highscore Ergebnisse

    private List<HighscoreController> entries;

    //Konstruktor wird Highscor Liste übergeben

    public HighscoreController(String name, int score){
        entries = new ArrayList<>();
    }

    //Methode zum Generieren von Ergebnissen

    public void addHighscore(String name, int score){
        entries.add(new HighscoreController(name, score);
    }
