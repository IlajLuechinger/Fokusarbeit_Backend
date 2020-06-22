package org.acme.quarkus.sample;

import java.sql.Date;

public class Task {

    private int id;
    private String titel;
    private String beschreibung;
    private String userStory;
    private double sollZeit;
    private double istZeit;
    private Date sollDate;
    private String status;
    private int personID;

    public Task(int id, String titel, String beschreibung, String userStory,
                double sollZeit, double istZeit, Date sollDate ,String status, int personID){
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.userStory = userStory;
        this.sollZeit = sollZeit;
        this.istZeit = istZeit;
        this.sollDate = sollDate;
        this.status = status;
        this.personID = personID;
    }

    public int getId() {
        return id;
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

    public Date getSollDate() {
        return sollDate;
    }

    public int getPersonID() {
        return personID;
    }

    public String getStatus() {
        return status;
    }
}
