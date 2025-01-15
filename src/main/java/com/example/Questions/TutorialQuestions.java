package com.example.Questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TutorialQuestions implements Question {

    private String correctAnswer;
    private List<String> incorrectAnswers;

    protected String Id;
    protected String QuestionText;
    protected String category;
    protected String difficulty;

    public TutorialQuestions(String QuestionText, String correctAnswer, List<String> incorrectAnswers) {
        this.QuestionText = QuestionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    // Getter und Setter

    public String getId() {
        return Id;
    }

    public String getQuestionText() {
        return QuestionText;
    }

    public String getCategory() {
        return category;
    }
    public String getDifficulty() {
        return difficulty;
    }

    public void setQuestion(String question) {
        this.QuestionText = QuestionText;
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
