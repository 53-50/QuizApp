package com.example.Interface;


// = gemeinsame Grundlage für versch, Controller/Modus
// damit alle Methoden einheitlich verwendet werden + verwendet werden können
//= gemeinsame Schnittstelle damit WinLoseLayout eingebaut werden kann

public interface ControllerBase {

    default int getPunkte() {
        return 0;
    }

    default int getLeben() {
        return 0;
    }

    default int getRightOnes() {
        return 0;
    }

    default int getQuestions() {
        return 0;
    }

    // default String getDifficulty() {return null; };

}
