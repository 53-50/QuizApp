package com.example.Questions;

// Importiert Gson-Klasse der Gson-Bibliothek
// für Konvertieren von JSON zu Java-Objekten (und umgekehrt) verwendet
import com.google.gson.Gson;

// Importiert TypeToken-Klasse, die es ermöglicht, komplexe Generics (z. B. List<T>)
// für die Gson-Deserialisierung zu definieren
import com.google.gson.reflect.TypeToken;

// Wird verwendet, um Dateien zeichenweise einzulesen.
import java.io.FileReader;
// Behandelt Eingabe-/Ausgabefehler.
import java.io.IOException;
// Repräsentiert einen generischen Typ in Java.
import java.lang.reflect.Type;
// Eine Sammlung von Fragen wird als List<TutorialQuestions> verarbeitet
import java.util.List;

public class QuestionLoader {

    public static List<TutorialQuestions> loadQuestionsFromJson(String filePath) {
        // JSON-Daten lesen und deserialisieren
        Gson gson = new Gson();

        // FileReader: Liest die JSON-Datei vom angegebenen Pfad filePath.
        // try mit Ressourcenschließung (try-with-resources): Stellt sicher, dass
        // FileReader nach der Verwendung automatisch geschlossen wird
        try (FileReader reader = new FileReader(filePath)) {

            // Erstellt Type, der die generische Typinformation für List<TutorialQuestions> enthält
            // Dies ist erforderlich, da Generics in Java zur Laufzeit gelöscht werden (Type Erasure
            // und Gson diese Information benötigt.
            Type questionListType = new TypeToken<List<TutorialQuestions>>() {}.getType();

            // Verwendet fromJson-Methode der Gson-Instanz, um
            // JSON-Daten in eine Liste von TutorialQuestions-Objekten zu deserialisieren.
            // reader: Der Dateiinhalt wird eingelesen.
            // questionListType: Typ, in den die Daten konvertiert werden (hier List<TutorialQuestions>).
            return gson.fromJson(reader, questionListType);
        } catch (IOException e) {
            // Fängt Fehler ab wenn Datei =/ existiert =/ lesbar ist od. anderer I/O-Fehler auftritt
            // e.printStackTrace(): Gibt Fehlermeldung auf der Konsole aus (nützlich für Debugging).
            // return null: Gibt null zurück, wenn Fehler auftritt.
            e.printStackTrace();
            return null;
        }
    }

}
