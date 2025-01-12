package com.example.Questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TutorialQuestions {

    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public TutorialQuestions(String question, String correctAnswer, List<String> incorrectAnswers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    // Getter und Setter
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    // Diese Methode kombiniert die richtige Antwort mit den falschen und mischt sie
    public List<String> getAllAnswers() {
        List<String> allAnswers = new ArrayList<>(incorrectAnswers);
        allAnswers.add(correctAnswer);
        Collections.shuffle(allAnswers); // Randomisieren der Antworten
        return allAnswers;
    }

}
