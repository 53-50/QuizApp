package com.example.Questions;

public class AnswerEvaluation {
    private final String question;
    private final String userAnswer;
    private final boolean isCorrect;
    private final String correctAnswer;

    public AnswerEvaluation(String question, String userAnswer, boolean isCorrect, String correctAnswer) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
