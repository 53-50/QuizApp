/*package com.example.Questions;

import java.util.List;

public abstract class AbstractQuestion {
    protected String id; // Eindeutige ID der Frage
    protected String questionText; // Fragetext
    protected String correctAnswer; // Korrekte Antwort
    protected List<String> incorrectAnswers; // Liste falscher Antworten

    // Konstruktor
    public AbstractQuestion(String id, String questionText, String correctAnswer, List<String> incorrectAnswers) {
        this.id = id;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    // Abstrakte Methoden (werden von den Subklassen implementiert)
    public abstract String getCategory();
    public abstract void setCategory(String category);
    public abstract String getDifficulty();
    public abstract void setDifficulty(String difficulty);

    // Gemeinsame Methode zum Anzeigen der Frage (kann überschrieben werden)
    public void displayQuestion() {
        System.out.println("Frage: " + questionText);
        System.out.println("Korrekte Antwort: " + correctAnswer);
        System.out.println("Falsche Antworten: " + incorrectAnswers);
    }
}*/