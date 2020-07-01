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
    private Set<ProjektName> projectNames = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));




    public ProjektResource(){
        getProjekts();
    }

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

    public Set<ProjektName> getProjectName(int id){
        Connection con = db.getDBCon();
        try {
            Statement statement = con.createStatement();
            String query = "Select Projekt_ID, Projektname  from Projekt " +
                    "where Projekt_ID = "  + id;
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                projectNames.clear();
                ProjektName projectName = new ProjektName( rs.getInt(1) ,rs.getString(2)) ;
                projectNames.add(projectName);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectNames;
    }

    public static void setPerson(Person person1){
        person = person1;
    }



    @GET
    public Set<Projekt> list() {
        return projekts;
    }

    @GET
    @Path("/id")
    public Set<Integer> listNumber(){ return projectID;}

    @POST
    @Path("/id")
    public Set<Integer> addID (int id){
        projectID.add(id);
        TaskResource.setProjectID(id);
        getProjectName(id);
        return projectID;
    }

    @GET
    @Path("/projectName")
    public Set<ProjektName> listProjectName(){
        return projectNames;
    }



}
