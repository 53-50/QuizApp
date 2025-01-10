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
            try {
                TriviaQuestion[] questions = gson.fromJson(json, TriviaQuestion[].class);
                return questions[0]; // Erste Frage zurückgeben
            } catch (JsonSyntaxException e) {
                throw new IOException("Error parsing JSON: " + e.getMessage());
            }
        } else {
            throw new IOException("API call failed with HTTP code: " + response.statusCode());
        }
    }
}