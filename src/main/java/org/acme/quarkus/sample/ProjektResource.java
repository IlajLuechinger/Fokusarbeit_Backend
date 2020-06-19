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

@Path("/projekte")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjektResource {


    private DBCon db = new DBCon();
    private Set<Projekt> projekts = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public ProjektResource(){
        getProjekts();
    }


    public Set<Projekt> getProjekts(){
        Connection con = db.getDBCon();
        try {
            Statement statement = con.createStatement();
            String query = "SELECT Projekt.Projektname FROM Projekt " +
                    "INNER JOIN Aufgabe ON Projekt.Projekt_ID = Aufgabe.FK_Projekt " +
                    "INNER JOIN PERSON ON Aufgabe.FK_Person = Person.Person_ID " +
                    "where Person.Person_ID = 1 " +
                    "Group by Projekt.Projekt_ID";
            ResultSet rs =  statement.executeQuery(query);
            while (rs.next()){
                Projekt projekt = new Projekt(rs.getString(1));
                projekts.add(projekt);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projekts;
    }

    @GET
    public Set<Projekt> list() {
        return projekts;
    }

}
