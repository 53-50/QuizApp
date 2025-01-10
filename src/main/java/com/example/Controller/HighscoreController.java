package com.example.Controller;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

    public class HighscoreController {

        @FXML
        private void onFilterChoice(ActionEvent event){
            MenuItem menuItem = (MenuItem) event.getSource();
            String chosenMode = menuItem.getText(); //Leicht, Mittel, Schwer
            applyFilter(chosenMode);
        }

        //Methode Filter-Logik - zeigt im TableView nur Einträge des passenden Spielmodus an
        private void applyFilter(String modus) {
        }

        //Methode für den gesetzten Filter Leicht
        public void onFilterLeicht(ActionEvent actionEvent) {
        }

        //Methode für den gesetzten Filter Mittel
        public void onFilterMittel(ActionEvent actionEvent) {
        }

        //Methode für den gesetzten Filter Schwer
        public void onFilterSchwer(ActionEvent actionEvent) {
        }

        //Methode für Zurück zum Menue
        public void onBackClick(ActionEvent actionEvent) {
        }
    }

