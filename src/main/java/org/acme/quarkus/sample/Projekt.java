package org.acme.quarkus.sample;

public class Projekt {

    private String projektName;
    private int id;

    public Projekt(String projektName, int id){
        this.projektName = projektName;
        this.id = id;
    }

    public String getProjektName() {
        return projektName;
    }

    public int getId() {
        return id;
    }
}
