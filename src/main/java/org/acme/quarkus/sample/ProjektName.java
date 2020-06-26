package org.acme.quarkus.sample;

public class ProjektName {

    private int id;
    private String projektName;

    public ProjektName(int id, String projektName){
        this.id = id;
        this.projektName = projektName;
    }

    public int getId() {
        return id;
    }

    public String getProjektName() {
        return projektName;
    }
}
