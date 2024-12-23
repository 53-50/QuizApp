package com.example.Controller;

import com.example.Services.TriviaApiService;
import com.example.Questions.TriviaQuestion;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class QuizController {

    private int questionCount = 0; // Tracks how many questions have been asked
    private static final int MAX_QUESTIONS = 10; // The total number of questions for the quiz
    private String difficulty = "easy"; // Default difficulty

    @FXML
    private Label feedbackLabel;
    @FXML
    private Label progressLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private Button answerBtn1;
    @FXML
    private Button answerBtn2;
    @FXML
    private Button answerBtn3;
    @FXML
    private Button answerBtn4;


    /*@FXML
    public void initialize() throws IOException {
       // loadNewQuestion();
    }*/

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty; // Set the difficulty based on user selection
    }

    void loadNewQuestion() throws IOException {
        if (questionCount >= MAX_QUESTIONS) {
            returnToMainMenu();
            return;
        }

            try {
                // Fetch a new question based on the selected difficulty
                TriviaQuestion question = TriviaApiService.fetchSingleQuestion(difficulty);

                //Increment question count
                questionCount++;

                // Update the progress label
                progressLabel.setText("Question " + questionCount + " of " + MAX_QUESTIONS);


                // Display the question
                questionLabel.setText(question.getQuestionText());

                // Combine and shuffle answers
                List<String> allAnswers = new ArrayList<>();
                allAnswers.add(question.getCorrectAnswer());
                allAnswers.addAll(question.getIncorrectAnswers());
                Collections.shuffle(allAnswers);

                // Assign answers to buttons
                answerBtn1.setText(allAnswers.get(0));
                answerBtn2.setText(allAnswers.get(1));
                answerBtn3.setText(allAnswers.get(2));
                answerBtn4.setText(allAnswers.get(3));

                // Store correct answer in button UserData
                answerBtn1.setUserData(allAnswers.get(0).equals(question.getCorrectAnswer()));
                answerBtn2.setUserData(allAnswers.get(1).equals(question.getCorrectAnswer()));
                answerBtn3.setUserData(allAnswers.get(2).equals(question.getCorrectAnswer()));
                answerBtn4.setUserData(allAnswers.get(3).equals(question.getCorrectAnswer()));

            } catch (IOException | InterruptedException e) {
                questionLabel.setText("Error: " + e.getMessage());
            }
    }


    //Methode ist, um zurück zum Hauptmenü zu kommen
    private void returnToMainMenu() {
        try {
            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            // Switch to the main menu scene
            Stage stage = (Stage) questionLabel.getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAnswerClick(ActionEvent actionEvent) throws IOException {
        Button clickedButton = (Button) actionEvent.getSource();
        boolean isCorrect = (boolean) clickedButton.getUserData();

        if (isCorrect) {
            feedbackLabel.setText("Correct! Well done.");
            feedbackLabel.setStyle("-fx-text-fill: green;"); // Set feedback text color to green
        } else {
            feedbackLabel.setText("Wrong! Try the next one.");
            feedbackLabel.setStyle("-fx-text-fill: red;"); // Set feedback text color to red
        }
        // Use Timeline to delay loading the next question
        Timeline timeline = new Timeline(new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(0.5), // Delay for 0.5 s
                event -> {
                    try {
                        loadNewQuestion(); // Load the next question
                        feedbackLabel.setText(""); // Clear the feedback label
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ));
        timeline.setCycleCount(1); // Execute the KeyFrame only once
        timeline.play(); // Start the timeline
    }

    public void resetQuiz() {
        questionCount = 0;
        progressLabel.setText("Question 1 of " + MAX_QUESTIONS); // Reset the label
    }
}

