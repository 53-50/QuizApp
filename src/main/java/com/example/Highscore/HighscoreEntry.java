package com.example.Highscore;

public class HighscoreEntry {
    private String playerName;
    private int score;
    private String difficulty;

    public HighscoreEntry(String playerName, int score, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
    }

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

