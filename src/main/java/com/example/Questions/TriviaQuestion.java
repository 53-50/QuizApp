package com.example.Questions;

import com.example.Interface.QuestionBase;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TriviaQuestion implements QuestionBase {
    private String correctAnswer; //Speichert korrekte Antwort, Aus Interface
    private List<String> incorrectAnswers; //Speichert inkorrekte Antwort, Aus Interface
    private String QuestionText; //Speichert Text der Frage, Aus Interface


    //@SerializedName: Gibt an, dass das JSON-Feld question mit dem Java-Feld APIQuestionData verknüpft werden soll.
    @SerializedName("question") //Damit können aus JSON die Fragen gelesen werden
    private APIQuestionData APIQuestionData; //Enthält die Daten des verschachtelten JSON-Objekts

    // Innere Klasse für das verschachtelte JSON-Objekt "question"
    public static class APIQuestionData {
        private String text; //Speichert den eigentlichen Text der Frage
        public String getText() { return text; } //Liefert Text zurück
        public void setText(String text) { this.text = text; }
    }


    // Getter/Setter

    public APIQuestionData getAPIQuestionData() { //Liefert die APIQuestionData zurück.
        return APIQuestionData;
    }

    public void setAPIQuestionData(TriviaQuestion.APIQuestionData APIQuestionData) {
        this.APIQuestionData = APIQuestionData;
    }


    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    //Liefert die richtige Antwort zurück. Diese Methode wird durch das QuestionBase-Interface gefordert.

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    @Override
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }
    //Liefert die Liste der falschen Antworten zurück (vom Interface gefordert).

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }


    @Override
    public String getQuestionText() {
        return QuestionText;
    }
    //Liefert den Text der Frage zurück (vom Interface gefordert).

    public void setQuestionText(String questionText) {
        QuestionText = questionText;
    }
}






