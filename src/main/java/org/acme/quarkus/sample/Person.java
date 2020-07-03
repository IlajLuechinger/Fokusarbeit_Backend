package org.acme.quarkus.sample;

public class Person {

    private int PersonID;
    private int RolleID;

    //Klasse um die Werte der angemeldeten Person zu speichern
    public Person(int PersonID, int RolleID){
        this.PersonID = PersonID;
        this.RolleID = RolleID;
        }



    public int getPersonID() {
        return PersonID;
    }

    public int getRolleID() {
        return RolleID;
    }
}
