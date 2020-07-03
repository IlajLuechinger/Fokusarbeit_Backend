package org.acme.quarkus.sample;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
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
    private Set<UpdateTask> updatedStatus = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Set<Integer> tasksID = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Set<Double> timeWorked = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private static int projektID;
    private int taskID;

    public TaskResource(){
        getTasks();
    }

    public double getIstTime(){
        Connection con = db.getDBCon();
        double istTime = 0;
        try {
            Statement statement = con.createStatement();
            String query = "Select  IstZeit from Aufgabe " +
                    "where Aufgabe_ID = " + taskID ;
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                istTime = rs.getDouble(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return istTime;
    }

    //Holt die Aufgaben je nach Nutzer, wenn der Benutzer ein Projektmanager ist bzw RolleID = 1 ist werden alle Aufgaben des Projekt geladen
    //Wenn die RolleID != 1 ist werden nur die Aufgaben der Person geladen.
    public Set<Task> getTasks(){
        Connection con = db.getDBCon();
        String query;
        try {
            Statement statement = con.createStatement();
            if(person.getRolleID() == 1){
                query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                        "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum ,Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                        "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                        "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                        "where Aufgabe.FK_Projekt = " + projektID ;
            } else {
                query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                        "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum, Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                        "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                        "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                        "where Aufgabe.FK_Projekt = " +  projektID +
                        " AND Person.Person_ID = " +  person.getPersonID();
            }
           ResultSet rs =  statement.executeQuery(query);
           while (rs.next()){
                Task task = new Task(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDate(7),
                        rs.getString(8), rs.getInt(9), rs.getInt(10));
                tasks.add(task);
            }
            rs.close();
           con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    //Holt nur die Aufgabe, welcher mit der ID überein stimmt
    public Set<Task> getSelectedTask(int id){
        Connection con = db.getDBCon();
        try {
            Statement statement = con.createStatement();
            String query = "Select Aufgabe.Aufgabe_ID, Aufgabe.Titel, Aufgabe.Beschreibung, Aufgabe.Userstory, " +
                    "Aufgabe.SollZeit, Aufgabe.IstZeit, Aufgabe.SollDatum, Status.Bezeichnung, Person.Person_ID, Aufgabe.FK_Projekt from Aufgabe " +
                    "Inner join Status on Aufgabe.FK_Status = Status.Status_ID " +
                    "Inner join Person on Aufgabe.FK_Person = Person.Person_ID " +
                    "where Aufgabe.Aufgabe_ID = " + id ;
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                selectedTask.clear();
                Task task = new Task(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDate(7),
                        rs.getString(8), rs.getInt(9), rs.getInt(10));
                selectedTask.add(task);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedTask;
    }

    //Updatet die gearbeitete Zeit
    private void updateIstTime(double totalTimeWorked) {
        Connection con = db.getDBCon();
        try {
            String query = "UPDATE Aufgabe " +
                    "SET IstZeit =" + totalTimeWorked +
                    " where Aufgabe_ID = " + taskID ;
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Updatet den Status der Aufgabe, falls sie auf dem Kanbanboard verschoben wurde
    private void updateTaskStatus(UpdateTask updateTask) {
        Connection con = db.getDBCon();
        try {
                String query = "UPDATE Aufgabe " +
                        "SET FK_Status =" + updateTask.getStatus() +
                        " where Aufgabe_ID = " + updateTask.getAufgabeID();
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @POST
    @Path("/updatedStatus")
    public Set<UpdateTask> addUpdatedStatus(UpdateTask updateTask){
        updatedStatus.add(updateTask);
        updateTaskStatus(updateTask);
        return updatedStatus;
    }

    @GET
    @Path("/updatedStatus")
    public Set<UpdateTask> listUpdatedStatus(){
        return updatedStatus;
    }




    @GET
    public Set<Task> list(){
        tasks.clear();
        return getTasks();
    }

    @POST
    @Path("/id")
    public Set<Integer> addID (int id){
        tasksID.add(id);
        getSelectedTask(id);
        taskID = id;
        return tasksID;
    }

    @GET
    @Path("/id")
    public Set<Integer> listNumber(){ return tasksID;}

    @GET
    @Path("/selectedTask")
    public Set<Task> listSelectedTask(){
        selectedTask.clear();
        return getSelectedTask(taskID);
    }

    @POST
    @Path("/timeWorked")
    public Set<Double> getTime(double time){
        timeWorked.add(time);
        double totalTimeWorked = getIstTime() + time;
        updateIstTime(totalTimeWorked);
        return timeWorked;
    }

    @GET
    @Path("/timeWorked")
    public Set<Double> listTime(){
        return timeWorked;
    }



    public static void setPerson(Person person1){
        person = person1;
    }

    public static void setProjectID(int projektID1){
        projektID = projektID1;
    }


}
