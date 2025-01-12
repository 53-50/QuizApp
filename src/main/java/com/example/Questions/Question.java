package com.example.Questions;

import java.util.List;

public interface Question {
    String getId();
    String getQuestionText();
    String getCorrectAnswer();
    List<String> getIncorrectAnswers();
    String getCategory();
    String getDifficulty();

}

