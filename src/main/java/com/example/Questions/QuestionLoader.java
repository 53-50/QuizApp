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
        // JSON-Daten lesen + deserialisieren
        Gson gson = new Gson();

        // FileReader: Liest JSON-Datei vom angegebenen Pfad filePath
        // try mit Ressourcenschließung (try-with-resources): Stellt sicher, dass
        // FileReader nach Verwendung automatisch geschlossen wird
        try (FileReader reader = new FileReader(filePath)) {

            // Erstellt Type, der generische Typinfo für List<TutorialQuestions> enthält
            // ist erforderlich, da Generics in Java zur Laufzeit gelöscht werden
            // und Gson diese Information benötigt
            Type questionListType = new TypeToken<List<TutorialQuestions>>() {}.getType();

            // fromJson-Methode der Gson-Instanz, um JSON-Daten in Liste von TutorialQuestions-Objekten deserialisieren
            // reader: Dateiinhalt wird eingelesen.
            // questionListType: Typ, in den Daten konvertiert werden (hier List<TutorialQuestions>).
            return gson.fromJson(reader, questionListType);

        } catch (IOException e) {
            // Fängt Fehler ab wenn Datei =/ existiert =/ lesbar ist od. anderer I/O-Fehler auftritt
            // e.printStackTrace(): Gibt Fehlermeldung auf Konsole aus
            // return null: Gibt null zurück, wenn Fehler auftritt
            e.printStackTrace();
            return null;
        }
    }

}
