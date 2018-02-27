package com.genie3.eventsLocation.models;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class EventMap
{
    private int id;
    private String nom;
    private String description;
    private ArrayList<Place> places;
    private User user;


    @JsonCreator
    public EventMap( @JsonProperty("nom") String nom,
                     @JsonProperty("description") String description) {
        this.nom = nom;
        this.description = description;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
