package com.genie3.eventsLocation.models;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventMap
{

    private int id;

    @NotNull(message = "Name field is not provide")
    @NotBlank(message = "Name must not be blank")
    private String  name;

    @NotNull(message = "Description field is not provide")
    private String description;


    private ArrayList<Place> places ;


    private User user;


    @JsonCreator
    public EventMap( @JsonProperty("name") String name,
                     @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
        this.places = new ArrayList<Place>();

    }

    public EventMap() {
        this.places = new ArrayList<Place>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nom) {
        this.name = nom;
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

    @Valid
    public User getUser() {
        return user;
    }


    public void setUser(@Valid User user) {
        this.user = user;
    }
}
