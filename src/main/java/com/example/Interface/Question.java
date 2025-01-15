package com.example.Interface;

import java.util.List;

public interface Question {
    // String getId();
    String getQuestionText();
    String getCorrectAnswer();
    List<String> getIncorrectAnswers();
    // String getCategory();
    String getDifficulty();

}

