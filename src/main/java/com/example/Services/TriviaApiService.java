/*
Resources:

API + Documentation:
- https://the-trivia-api.com/docs/v2/

Video Resources:
How To Call a REST API In Java - Simple Tutorial (c) Coding with John
(https://www.youtube.com/watch?v=9oq7Y8n1t00)

Quiz App Using Vanilla JavaScript | With Open Trivia DB API (c) GeekProbin
(https://www.youtube.com/watch?v=-cX5jnQgqSM&t=889s)

Verweis auf Verwendung von "KI":
Der folgende Code wurde mithilfe des "KI"-Modells ChatGPT-4o (c) OpenAI
anhand der oben dargelegten Resourcen entwickelt, gereviewed und implementiert.

*/

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

    // Basis-URL - sodass nur eine Frage pro Anfrage an API zurückgegeben wird
    //Es wird ein HttpClient erstellt, der für den Versand der HTTP-Anfrage zuständig ist
    //Erstellt ein Gson-Objekt, das für die Umwandlung (Serialisierung/Deserialisierung) vbon JSON-Daten verwendet wird

    public static TriviaQuestion fetchSingleQuestion(String APIdifficulty) throws IOException, InterruptedException {
        String apiUrl = BASE_URL + "&difficulties=" + APIdifficulty;
        //Der Methode wird ein difficulty-parameter übergeben, der an die Base-URL angehängt wird, um
        //eine Frage der gewünschten Schwierigkeit auszugeben


        //Eine HTTP-Anfrage wird an die API geschickt
        HttpRequest request = HttpRequest.newBuilder()      //HTTP-Anfrage Builder
                .uri(URI.create(apiUrl))                    //Legt URI (Uniform Resource Identifier) der Anfrage fest
                .GET()                                      //Definiert diese Anfrage als eine GET-Anfrage
                .build();                                   //Baut das HttpRequest-Objkekt zusammen


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //Hier wird die Anfrage gesendet, und auf die Antwort gewartet
        //HttpResponse.BodyHandlers.ofString() -> Gibt schlussendlich die Antwort als String zurück
        //Das Ergebnis wird in "response" gespeichert

        /*
        // ~DEBUGGING~: HTTP-Code + JSON DEBUGGING
        System.out.println("~DEBUGGING - API~"); //Debugging
        System.out.println("HTTP code: " + response.statusCode()); //Debugging
        System.out.println("Response body: " + response.body()); //Debugging
        System.out.println("=== RAW JSON ==="); //Debugging
        System.out.println(response.body()); //Debugging
        System.out.println("===============\n"); //Debugging
        */


        if (response.statusCode() == 200) {             //Prüft, ob Anfrage erfolgreich war (200 = OK)
            String json = response.body();              //Hier wird der json string aus response geholt

            try {
                // JSON in ein Array von TriviaQuestion-Objekten mappen
                TriviaQuestion[] questions = gson.fromJson(json, TriviaQuestion[].class);
                //TriviaQuestion[].class gibt an, dass JSON-String in Array von TriviaQuestion-Objekten deserialisiert wird

                if (questions != null && questions.length > 0) { //Prüft, dass Array nicht leer ist

                    //DEBUGGING
                    //System.out.println("~DEBUGGING~ Deserialized question ID: " + questions[0].getId()); //Debugging
                    //System.out.println("~DEBUGGING~ Deserialized question text: " + questions[0].getQuestionText()); //Debugging

                    return questions[0]; //Da nur eine Frage angefordert - gibt das 1. Element des Arrays zurück
                } else {
                    throw new IOException("No questions returned from API."); //Exception, wenn keine Frage da ist
                }

            } catch (JsonSyntaxException e) { //Fängt parsing fehler ab
                throw new IOException("Error parsing JSON: " + e.getMessage(), e); //Wirft fehlermeldung
            }
        } else {
            throw new IOException("API call failed with HTTP code: " + response.statusCode());
            //Wirft fehler, wenn Http-Anfrage nicht erfolgreich war
        }
    }
}

