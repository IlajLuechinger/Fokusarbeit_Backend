package org.acme.quarkus.sample;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjektResource {


    private static Person person;
    private DBCon db = new DBCon();
    private Set<Projekt> projekts = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Set<Integer> projectID = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Set<Projekt> projectNames = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));



    //Beim initalisieren werden direkt die Projekte geladen
    public ProjektResource(){
        getProjekts();
    }

    //Ladet die Projekte
    public Set<Projekt> getProjekts(){
        Connection con = db.getDBCon();
        try {
            Statement statement = con.createStatement();
            String query = "SELECT Projektname, Projekt_ID FROM Projekt " +
                    "INNER JOIN Aufgabe ON Projekt.Projekt_ID = Aufgabe.FK_Projekt " +
                    "INNER JOIN PERSON ON Aufgabe.FK_Person = Person.Person_ID " +
                    "where Person.Person_ID = " + person.getPersonID() +
                    " Group by Projekt.Projekt_ID";
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                Projekt projekt = new Projekt(rs.getString(1), rs.getInt(2));
                projekts.add(projekt);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projekts;
    }

    //Ladet die Projektnamen basieren auf der ID, welche man vom Frontend bekommt
    public Set<Projekt> getProjectName(int id){
        Connection con = db.getDBCon();
        try {
            Statement statement = con.createStatement();
            String query = "Select Projekt_ID, Projektname  from Projekt " +
                    "where Projekt_ID = "  + id;
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                //Lösch den vorherigen Projektnamen, da man immer nur einen benötigt
                projectNames.clear();
                Projekt projectName = new Projekt(rs.getString(2), rs.getInt(1)) ;
                projectNames.add(projectName);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectNames;
    }

    //Klassenvariabel einer Person benötigt um die richtige Projekte zu ladne
    public static void setPerson(Person person1){
        person = person1;
    }



    //Zeigt die Projekte auf localhost:8080/projects an
    @GET
    public Set<Projekt> list() {
        projekts.clear();
        return getProjekts();
    }

    //Zeigt die ProjektID auf localhost:8080/projects/id an
    @GET
    @Path("/id")
    public Set<Integer> listNumber(){ return projectID;}

    //Entnimmt den Wert von localhost:8080/projects/id
    @POST
    @Path("/id")
    public Set<Integer> addID (int id){
        projectID.add(id);
        TaskResource.setProjectID(id);
        getProjectName(id);
        return projectID;
    }

    //Sendet den ausgewählten Projektnamen auf ocalhost:8080/projects/projectName
    @GET
    @Path("/projectName")
    public Set<Projekt> listProjectName(){
        return projectNames;
    }



}
