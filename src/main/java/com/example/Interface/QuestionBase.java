package com.example.Interface;

import java.util.List;

public interface QuestionBase {
    String getQuestionText();
    String getCorrectAnswer();
    List<String> getIncorrectAnswers();
}

