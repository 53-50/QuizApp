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

    /**
     * Fetches a single trivia question from the API with the given difficulty.
     *
     * @param difficulty The difficulty level ("easy", "medium", "hard").
     * @return The first trivia question returned by the API.
     * @throws IOException          If there is an issue with the API call or response.
     * @throws InterruptedException If the HTTP request is interrupted.
     */

    public static TriviaQuestion fetchSingleQuestion(String difficulty) throws IOException, InterruptedException {
        // Construct the API URL with the difficulty parameter
        String apiUrl = BASE_URL + "&difficulties=" + difficulty;

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        // Send the HTTP request and get the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String json = response.body();

            try {
                // Parse the JSON response into an array of TriviaQuestion objects
                TriviaQuestion[] questions = gson.fromJson(json, TriviaQuestion[].class);
                return questions[0]; // Return the first question
            } catch (JsonSyntaxException e) {
                throw new IOException("Error parsing JSON: " + e.getMessage());
            }
        } else {
            throw new IOException("API call failed: HTTP " + response.statusCode());
        }
    }
}

