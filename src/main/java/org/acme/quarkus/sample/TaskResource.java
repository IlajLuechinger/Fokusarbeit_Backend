package org.acme.quarkus.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    private DBCon db = new DBCon();
    private static Person person;
    private Set<Task> tasks = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public TaskResource(){
        getTasks();
    }

    public Set<Task> getTasks(){
        Connection con = db.getDBCon();
        String query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum, Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                "where Aufgabe.FK_Projekt = 1 " +
                "AND Person.Person_ID = 1";
        try {
            Statement statement = con.createStatement();
            if(true){
                query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                        "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum ,Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                        "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                        "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                        "where Aufgabe.FK_Projekt = 1;";
            }
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                Task task = new Task(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDate(7),
                        rs.getString(8), rs.getInt(9), rs.getInt(10));
                tasks.add(task);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static void setPerson(Person person1){
        person = person1;
    }

    @GET
    public Set<Task> list(){
        return tasks;
    }

}
