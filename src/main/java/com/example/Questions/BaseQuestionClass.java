package com.example.Questions;

import java.util.List;

public abstract class BaseQuestionClass implements Question {
        protected String id;
        protected String questionText;
        protected String correctAnswer;
        protected List<String> incorrectAnswers;
        protected String category;
        protected String difficulty;

        public BaseQuestionClass(String id, String questionText, String correctAnswer, List<String> incorrectAnswers,
                                String category, String difficulty) {
            this.id = id;
            this.questionText = questionText;
            this.correctAnswer = correctAnswer;
            this.incorrectAnswers = incorrectAnswers;
            this.category = category;
            this.difficulty = difficulty;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getQuestionText() {
            return questionText;
        }

        @Override
        public String getCorrectAnswer() {
            return correctAnswer;
        }

        @Override
        public List<String> getIncorrectAnswers() {
            return incorrectAnswers;
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public String getDifficulty() {
            return difficulty;
        }
    }
