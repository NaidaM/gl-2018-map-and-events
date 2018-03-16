package com.genie3.eventsLocation.models;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventMap
{

    private String id;

    @NotNull(message = "Name field is not provide")
    @NotBlank(message = "Name must not be blank")
    private String  name;

    @NotNull(message = "Description field is not provide")
    private String description;


    private ArrayList<Place> places ;


    private User user;



    @NotNull
    @Pattern(regexp="\\[[_ A-Za-z0-9 ]*(,[_ A-Za-z0-9]+)*\\]")
    @JsonIgnore
    private String taglist;



    private ArrayList<String> tags;



    @NotNull (message = "visibility field is not provide")
    @Pattern(regexp = "^true|false$+",message = "Private must be a boolean")
    private String visibility;


    @JsonCreator
    public EventMap( @JsonProperty("name") String name,
                     @JsonProperty("description") String description,
                     @JsonProperty("visibility") String isPrivate,
                     @JsonProperty("taglist") String tags
                     ) {
        this.name = name;
        this.description = description;
        this.places = new ArrayList<Place>();
        this.setVisibility(isPrivate);
        this.taglist = tags;
        this.user = null;

    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void toArray(String tags){
        String s = tags.replaceAll("\\[","")
                .replaceAll("\\]","");

        if (s.isEmpty()){

            this.setTags(new ArrayList<String>());
        }else {
            String tmp[] =s.split(",");

            ArrayList<String> mapTag = new ArrayList<String>();

            for (int i = 0 ;i <tmp.length ; i++){
                mapTag.add(tmp[i]);
            }

          //  System.out.println("size" + mapTag.size());
            this.setTags(mapTag);
        }


    }


    public EventMap() {
        this.places = new ArrayList<Place>();
        this.user = null;
        this.tags = new ArrayList<String>();
    }


    public String getTaglist() {
        return taglist;
    }

    public void setTaglist(String taglist) {
        this.taglist = taglist;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
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


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public Boolean  isPrivate() {
        return Boolean.valueOf(visibility);
    }

    public void setVisibility(String aPrivate) {
        visibility = aPrivate;
    }
    public String getVisibility() {
        return visibility;
    }
}
