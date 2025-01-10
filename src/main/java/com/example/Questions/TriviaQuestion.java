package com.example.Questions;

import java.util.List;
/*

public class TriviaQuestion   {
    private String category; // Kategorie der Frage
    private String difficulty; // Schwierigkeitsgrad
    private Question question; // Das verschachtelte Objekt für den Fragetext
    private List<String> tags; // API-spezifisch
    private List<String> regions; // API-spezifisch

    public TriviaQuestion(String id, Question question, String correctAnswer, List<String> incorrectAnswers,
                          String category, String difficulty, List<String> tags, List<String> regions) {
        super(id, question.getText(), correctAnswer, incorrectAnswers); // Ruft den Konstruktor der Basisklasse auf
        this.category = category;
        this.difficulty = difficulty;
        this.question = question;
        this.tags = tags;
        this.regions = regions;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    // Nested class for the Question object
    public static class Question {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
*/

/*
public class TriviaQuestion extends AbstractQuestion {
    private String category; // Kategorie der Frage
    private String difficulty; // Schwierigkeitsgrad
    private List<String> tags; // API-spezifisch
    private List<String> regions; // API-spezifisch

    public TriviaQuestion(String id, String questionText, String correctAnswer, List<String> incorrectAnswers,
                          String category, String difficulty, List<String> tags, List<String> regions) {
        super(id, questionText, correctAnswer, incorrectAnswers); // Ruft den Konstruktor der Basisklasse auf
        this.category = category;
        this.difficulty = difficulty;
        this.tags = tags;
        this.regions = regions;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // Zusätzliche Methoden für API-spezifische Felder
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }
}


*/


public class TriviaQuestion {
    private String id;
    private Question question; // Verschachteltes Objekt für den Fragetext
    private String correctAnswer;
    private List<String> incorrectAnswers;
    private String category;
    private String difficulty;
    private List<String> tags;
    private List<String> regions;

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    // Frageobjekt
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    // Getter für den Fragetext
    public String getQuestionText() {
        return question != null ? question.getText() : "No question text available.";
    }

    public static class Question {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}

