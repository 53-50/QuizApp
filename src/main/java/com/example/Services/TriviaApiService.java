// API from https://the-trivia-api.com/docs/v2/

package com.example.Services;

import com.example.Questions.TriviaQuestion;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import com.example.Questions.TriviaQuestion;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TriviaApiService {
    private static final String BASE_URL = "https://the-trivia-api.com/v2/questions?limit=1";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static TriviaQuestion fetchSingleQuestion(String difficulty) throws IOException, InterruptedException {
        // difficulty-Parameter anhängen
        String apiUrl = BASE_URL + "&difficulties=" + difficulty;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("=== RAW JSON ===");
        System.out.println(response.body());
        System.out.println("===============\n");

        // Debug: Zeig mal HTTP-Code + JSON
        System.out.println("HTTP code: " + response.statusCode());
        System.out.println("Response body: " + response.body());

        if (response.statusCode() == 200) {
            String json = response.body();

            try {
                // JSON in ein Array von TriviaQuestion mappen
                TriviaQuestion[] questions = gson.fromJson(json, TriviaQuestion[].class);

                if (questions != null && questions.length > 0) {

                    //System.out.println("Deserialized question ID: " + questions[0].getId()); //Debugging
                    System.out.println("Deserialized question text: " + questions[0].getQuestionText()); //Debugging

                    return questions[0];
                } else {
                    throw new IOException("No questions returned from API.");
                }

            } catch (JsonSyntaxException e) {
                throw new IOException("Error parsing JSON: " + e.getMessage(), e);
            }
        } else {
            throw new IOException("API call failed with HTTP code: " + response.statusCode());
        }
    }
}


/*

public class TriviaApiService {
    private static final String BASE_URL = "https://the-trivia-api.com/v2/questions?limit=1";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static TriviaQuestion fetchSingleQuestion(String difficulty) throws IOException, InterruptedException {
        String apiUrl = BASE_URL + "&difficulties=" + difficulty;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String json = response.body();
            System.out.println("API Response: " + json); // Debugging

            try {
                TriviaQuestion[] questions = gson.fromJson(json, TriviaQuestion[].class);
                if (questions.length == 0) throw new IOException("No questions found in API response");
                return questions[0];
            } catch (JsonSyntaxException e) {
                throw new IOException("Error parsing JSON: " + e.getMessage());
            }
        } else {
            throw new IOException("API call failed with HTTP code: " + response.statusCode());
        }
    }
}
*/