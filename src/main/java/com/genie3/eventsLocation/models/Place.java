package com.genie3.eventsLocation.models;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public  class Place {



    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private EventMap map;


    @JsonCreator
    public Place( @JsonProperty("name") String name,
                  @JsonProperty("description") String description,
                  @JsonProperty("latitude") double latitude,
                  @JsonProperty("longitude") double longitude
                  ) {

        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventMap getMap() {
        return map;
    }

    public void setMap(EventMap map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", map=" + map +
                '}';
    }
}
