package org.acme.quarkus.sample;
//import io.quarkus.launcher.shaded.org.apache.commons.codec.binary.Hex;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    private DBCon db = new DBCon();
    private Set<Person> persons = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Person person;

    public Set<Person> getLoginPerson(){
        Connection con = db.getDBCon();
        try {
        Statement statement = con.createStatement();
        String query = "Select Person_ID, FK_Rolle from Person " +
                "where Email = 'ilaj1212@gmail.com' " +
                "AND password = '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4'";
        ResultSet rs =  statement.executeQuery(query);
        while (rs.next()){
            person = new Person(rs.getInt(1) ,rs.getInt(2));
            System.out.println(person.getPersonID() + " " + person.getRolleID());
            persons.add(person);
        }
        rs.close();
    } catch (
    SQLException e) {
        e.printStackTrace();
    }
        return persons;
    }

    public String hashPW(String pw){
        MessageDigest digest = null;
        String hexString = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
            //hexString = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    @GET
    public Set<Person> list(){
        return getLoginPerson();
    }

    public Person getPerson(){
        return person;
    }
}
