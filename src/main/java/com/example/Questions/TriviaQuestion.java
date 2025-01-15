package com.example.Questions;

import com.example.Interface.Question;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TriviaQuestion implements Question {

    protected String correctAnswer;
    protected List<String> incorrectAnswers;
    protected String QuestionText;
    protected String difficulty;

    // Der Feldname muss 'question' sein, um mit dem JSON übereinzustimmen
    @SerializedName("question")
    private APIQuestionData APIQuestionData;

    // Setter für 'APIQuestionData', wird von Gson aufgerufen
    public void setAPIQuestionData(APIQuestionData APIQuestionData) {
        this.APIQuestionData = APIQuestionData;
        if (APIQuestionData != null) {
            this.QuestionText = APIQuestionData.getText();
            // Debug-Ausgabe
            System.out.println("Setter 'APIQuestionData' aufgerufen. FrageText gesetzt auf: " + this.QuestionText);
        } else {
            System.out.println("Setter 'APIQuestionData' aufgerufen, aber Frage ist null.");
        }
    }

    // Getter für 'APIQuestionData' (optional)
    public APIQuestionData getAPIQuestionData() {
        return APIQuestionData;
    }

    @Override
    public String getQuestionText() {
        return QuestionText;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    // Innere Klasse für das verschachtelte JSON-Objekt "question"
    public static class APIQuestionData {
        private String text;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}




/*

public class TriviaQuestion extends BaseQuestionClass {

    private Question question;

    public TriviaQuestion(String id, Question question, String correctAnswer, List<String> incorrectAnswers,
                          String category, String difficulty) {
        super(id, question.getText(), correctAnswer, incorrectAnswers, category, difficulty);
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
        this.questionText = question.getText(); // Aktualisiert den Fragetext
    }

    // Innere Klasse für die API-Struktur
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

}

*/