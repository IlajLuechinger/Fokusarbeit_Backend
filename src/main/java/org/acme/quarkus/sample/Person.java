package org.acme.quarkus.sample;

public class Person {

    private int PersonID;
    private int RolleID;

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
