package com.example.Interface;


// = gemeinsame Grundlage für versch, Controller/Modus
// damit alle Methoden einheitlich verwendet werden + verwendet werden können
//= gemeinsame Schnittstelle damit WinLoseLayout eingebaut werden kann

public interface ControllerBase {

    default int getPoints() {
        return 0;
    }

    default int getLives() {
        return 0;
    }

    default int getRightOnes() {
        return 0;
    }

    default int getQuestions() {
        return 0;
    }

}
