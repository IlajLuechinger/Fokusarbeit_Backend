package org.acme.quarkus.sample;


public class Login {

    private String password;
    private String email;

    public Login(){}

    //Klasse um die Login Daten zu empfangen
    public Login(String password, String email){
        this.password = password;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
