package com.genie3.eventsLocation.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genie3.eventsLocation.constraints.ValidTag;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public  class Place {



    private String id;

    @NotNull(message = "Name field must be provide")
    @NotBlank(message = "Provide a name for a place")
    private String name;

    @NotNull(message = "Latitude must be provide")
    @Pattern(regexp = "^[0-9]+(.[0-9]+)?$+",message = "Latitude must be a number")
    private String latitude;

    @NotNull(message = "Longitude must be provide")
    @Pattern(regexp = "^[0-9]+(.[0-9]+)?$",message = "Longitude must be a number")
    private String longitude;

    @NotNull
    @NotBlank
    private String description;

    @JsonIgnore
    private EventMap map;


    @NotNull
    @Pattern(regexp="\\[[_ A-Za-z0-9 ]*(,[_ A-Za-z0-9]+)*\\]")
    @JsonIgnore
    private String taglist;


    private  String[] tags ;


    @JsonCreator
    public Place( @JsonProperty("name") String name,
                  @JsonProperty("description") String description,
                  @JsonProperty("latitude") String latitude,
                  @JsonProperty("longitude") String longitude,
                  @JsonProperty("taglist") String tags
                  ) {

        this.name = name;
        this.description = description;
        this.longitude =longitude;
        this.latitude = latitude;
        this.map = null;
        this.taglist = tags;

    }

    public Place() {
        this.map = null;
        this.tags = new String[1];
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
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

    public String getTaglist() {
        return taglist;
    }

    public void setTaglist(String taglist) {
        this.taglist = taglist;
    }

    public void toArray(String tags){
        String s = tags.replaceAll("\\[","")
                .replaceAll("\\]","");

        String tmp[] =s.split(",");

        this.setTags(tmp);

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
