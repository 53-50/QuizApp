package com.example.Questions;

import com.example.Interface.QuestionBase;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TriviaQuestion implements QuestionBase {

    protected String correctAnswer;
    protected List<String> incorrectAnswers;
    protected String QuestionText;
    //protected String difficulty;

    // Der Feldname muss 'question' sein, um mit dem JSON übereinzustimmen
    @SerializedName("question")
    private APIQuestionData APIQuestionData;

    /*
    // Setter für 'APIQuestionData', wird von Gson aufgerufen
    public void setAPIQuestionData(APIQuestionData APIQuestionData) {
        this.APIQuestionData = APIQuestionData;
        if (APIQuestionData != null) {
            this.QuestionText = APIQuestionData.getText();
            // Debug-Ausgabe
            System.out.println("Setter ‘APIQuestionData’ called. QuestionText set to: " + this.QuestionText);
        } else {
            System.out.println("Setter ‘APIQuestionData’ called, but question is null.");
        }
    }
*/

    // Getter für 'APIQuestionData' (optional)
    public APIQuestionData getAPIQuestionData() {
        return APIQuestionData;
    }

    //TODO Setter generieren


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

    /*
    @Override
    public String getDifficulty() {
        return difficulty;
    }
*/

    // Innere Klasse für das verschachtelte JSON-Objekt "question"
    public static class APIQuestionData {
        private String text;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}

