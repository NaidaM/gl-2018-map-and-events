package com.genie3.eventsLocation.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

public class User
{

    private String pseudo;
    private String email;
    private String password;
    private ArrayList<EventMap> maps;

    @JsonCreator
    public User( @JsonProperty("pseudo") String pseudo,
                 @JsonProperty("email") String email,
                 @JsonProperty("password") String password,
                 @JsonProperty("maps") ArrayList<EventMap> maps ){
        this.pseudo=pseudo;
        this.email=email;
        this.password = password;
        this.maps = maps;

    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<EventMap> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<EventMap> maps) {
        this.maps = maps;
    }
}
