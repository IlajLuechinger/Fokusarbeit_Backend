package org.acme.quarkus.sample;

public class UpdateTask {

    private int aufgabeID;
    private int status;

    public UpdateTask(){

    }
    //Klasse mit den wichtigen Parameter um eine Aufgabe zu aktualisieren
    public UpdateTask(int status, int aufgabeID){
        this.aufgabeID = aufgabeID;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getAufgabeID() {
        return aufgabeID;
    }

    public void setAufgabeID(int aufgabeID) {
        this.aufgabeID = aufgabeID;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
