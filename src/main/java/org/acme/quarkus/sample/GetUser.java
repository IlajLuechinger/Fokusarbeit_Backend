package org.acme.quarkus.sample;
import io.quarkus.launcher.shaded.org.apache.commons.codec.binary.Hex;

import javax.ws.rs.*;
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

@Path("/loginData")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GetUser {

    private DBCon db = new DBCon();
    private Set<Login> logins = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Set<Person> people = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private Person person;
    private Login login;

    public Set<Person> getUserIfExist(Login login){
        String pw = login.getPassword();
        String hashedPW = hashPW(pw);
        String mail = login.getEmail();
        System.out.println(pw + " "  + mail);
        Connection con = db.getDBCon();
        try {
            Statement statement = con.createStatement();
            String query = "Select Person_ID, FK_Rolle from Person " +
            "where Email =" + " '"+ mail + "' " +
            "and password =" + "'" + hashedPW + "'";
            ResultSet rs =  statement.executeQuery(query);
            if (rs.next()){
                person = new Person(rs.getInt(1), rs.getInt(2));
                people.add(person);
                System.out.println(person.getPersonID());
                ProjektResource.setPerson(person);
            } else {
                person = new Person(0,0);
                people.add(person);
            }
            rs.close();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return people;
    }


    @GET
    public Set<Login> list(){
        return logins;
    }

    @POST
    public Set<Login> add(Login login){
        this.login = login;
        logins.add(login);
        getUserIfExist(login);
        return logins;
    }

    @GET
    @Path("/user")
    public Set<Person> listPerson(){
        return people;
    }

    public String hashPW(String pw){
        MessageDigest digest = null;
        String hexString = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
            hexString = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public Person getLoginPerson(){
        return person;
    }

    public Login getLogin() {
        return login;
    }
}