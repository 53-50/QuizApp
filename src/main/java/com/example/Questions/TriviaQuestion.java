package com.example.Questions;

import com.example.Interface.QuestionBase;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TriviaQuestion implements QuestionBase {
    private String correctAnswer; //Aus Interface
    private List<String> incorrectAnswers; //Aus Interface
    private String QuestionText; //Aus Interface


    // Der Feldname muss 'question' sein, um mit dem JSON übereinzustimmen
    @SerializedName("question") //Damit aus dem gson zum Json gelesen werden kann -> Fragen
    private APIQuestionData APIQuestionData;

    // Innere Klasse für das verschachtelte JSON-Objekt "question"
    public static class APIQuestionData {
        private String text;
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }


    // Getter/Setter

    public APIQuestionData getAPIQuestionData() {
        return APIQuestionData;
    }

    public void setAPIQuestionData(TriviaQuestion.APIQuestionData APIQuestionData) {
        this.APIQuestionData = APIQuestionData;
    }


    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    @Override
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }


    @Override
    public String getQuestionText() {
        return QuestionText;
    }

    public void setQuestionText(String questionText) {
        QuestionText = questionText;
    }
}






