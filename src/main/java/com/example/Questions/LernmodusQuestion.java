package com.example.Questions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LernmodusQuestion extends AbstractQuestion {
    private String category; // Kategorie, z. B. für das Lernen relevant

    public LernmodusQuestion(String id, String questionText, String correctAnswer, List<String> incorrectAnswers, String category) {
        super(id, questionText, correctAnswer, incorrectAnswers);
        this.category = category;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getDifficulty() {
        return "N/A"; // Schwierigkeit wird im Lernmodus nicht verwendet
    }

    @Override
    public void setDifficulty(String difficulty) {
        // Keine Aktion erforderlich
    }

    // CSV-Import
    public static List<LernmodusQuestion> importFromCsv(String filePath) throws IOException {
        List<LernmodusQuestion> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String questionText = parts[1];
                String correctAnswer = parts[2];
                List<String> incorrectAnswers = List.of(parts[3], parts[4], parts[5]);
                String category = parts[6];
                questions.add(new LernmodusQuestion(id, questionText, correctAnswer, incorrectAnswers, category));
            }
        }
        return questions;
    }

    // CSV-Export
    public static void exportToCsv(String filePath, List<LernmodusQuestion> questions) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (LernmodusQuestion question : questions) {
                String line = question.getId() + "," +
                        question.getQuestionText() + "," +
                        question.getCorrectAnswer() + "," +
                        String.join(",", question.getIncorrectAnswers()) + "," +
                        question.getCategory();
                bw.write(line);
                bw.newLine();
            }
        }
    }
}