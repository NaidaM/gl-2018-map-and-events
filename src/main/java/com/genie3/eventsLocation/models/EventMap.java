package com.genie3.eventsLocation.models;

import java.util.ArrayList;

public class EventMap
{
    private String nom;
    private String description;
    private ArrayList<Place> places;
    private User user;

    public EventMap(String nom, String description, User user) {
        this.nom = nom;
        this.description = description;
        this.user = user;
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
