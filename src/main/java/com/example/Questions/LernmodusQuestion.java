package com.example.Questions;

public class LernmodusQuestion {
    private String question;
    private String answer;

    public LernmodusQuestion (String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}