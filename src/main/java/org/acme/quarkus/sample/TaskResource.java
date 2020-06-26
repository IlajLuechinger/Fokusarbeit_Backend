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

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    private DBCon db = new DBCon();
    private static Person person;
    private Set<Task> tasks = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Set<Task> selectedTask = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Set<Integer> taskID = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private static int projektID;


    public TaskResource(){
        getTasks();
    }

    public Set<Task> getTasks(){
        Connection con = db.getDBCon();
        String query;
        try {
            Statement statement = con.createStatement();
            System.out.println(person.getRolleID());
            if(person.getRolleID() == 1){
                query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                        "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum ,Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                        "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                        "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                        "where Aufgabe.FK_Projekt = " + projektID ;
                System.out.println("Bin Projektmanager");
            } else {
                query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                        "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum, Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                        "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                        "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                        "where Aufgabe.FK_Projekt = " +  projektID +
                        " AND Person.Person_ID = " +  person.getPersonID();
            }
           ResultSet rs =  statement.executeQuery(query);
            System.out.println(rs.next());
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

    public Set<Task> getSelectedTask(int id){
        Connection con = db.getDBCon();

        try {
            Statement statement = con.createStatement();
            System.out.println(id);
            String query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                    "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum, Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                    "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                    "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                    "where Aufgabe.Aufgabe_ID = " + id ;
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                Task task = new Task(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDate(7),
                        rs.getString(8), rs.getInt(9), rs.getInt(10));
                selectedTask.add(task);
                System.out.println("Nochmal JA");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedTask;
    }



    @GET
    public Set<Task> list(){
        return tasks;
    }



    @POST
    @Path("/id")
    public Set<Integer> addID (int id){
        taskID.add(id);
        getSelectedTask(id);
        return taskID;
    }

    @GET
    @Path("/id")
    public Set<Integer> listNumber(){ return taskID;}

    @GET
    @Path("/selectedTask")
    public Set<Task> listSelectedTask(){
        return selectedTask;
    }

    public static void setPerson(Person person1){
        person = person1;
    }

    public static void setProjectID(int projektID1){
        projektID = projektID1;
    }


}
