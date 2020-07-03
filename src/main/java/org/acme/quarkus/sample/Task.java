package org.acme.quarkus.sample;

import java.sql.Date;

public class Task {

    private int aufgabeID;

    private String titel;
    private String beschreibung;
    private String userStory;
    private double sollZeit;
    private double istZeit;
    private Date sollDatum;
    private String status;
    private int personID;
    private int projektID;


    //Task-Klasse um die Aufgaben zu speichern
    public Task(int aufgabeID, String titel, String beschreibung, String userStory,
                double sollZeit, double istZeit, Date sollDatum ,String status, int personID, int projektID){
        this.aufgabeID = aufgabeID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.userStory = userStory;
        this.sollZeit = sollZeit;
        this.istZeit = istZeit;
        this.sollDatum = sollDatum;
        this.status = status;
        this.personID = personID;
        this.projektID = projektID;
    }


    public int getAufgabeID() {
        return aufgabeID;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public double getSollZeit() {
        return sollZeit;
    }

    public String getTitel() {
        return titel;
    }

    public String getUserStory() {
        return userStory;
    }

    public double getIstZeit() {
        return istZeit;
    }

    public Date getSollDatum() {
        return sollDatum;
    }

    public int getPersonID() {
        return personID;
    }

    public String getStatus() {
        return status;
    }

    public int getProjektID() {
        return projektID;
    }
}
