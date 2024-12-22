// API from https://the-trivia-api.com/docs/v2/

package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TriviaApiService {

    private static final String API_URL = "https://the-trivia-api.com/v2/questions?limit=1";

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static TriviaQuestion fetchSingleQuestion(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String json = response.body();

            try {
                // Die API liefert ein JSON-Array mit Fragen
                TriviaQuestion[] questions = gson.fromJson(json, TriviaQuestion[].class);
                return questions[0];  // Wir laden nur eine Frage (limit=1)
            } catch (JsonSyntaxException e) {
                throw new IOException("Fehler beim Parsen des JSON: " + e.getMessage());
            }
        } else {
            throw new IOException("Fehler beim API-Aufruf: HTTP " + response.statusCode());
        }
    }
}
