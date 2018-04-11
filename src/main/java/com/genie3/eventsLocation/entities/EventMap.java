package com.genie3.eventsLocation.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventMap
{

    private String id;

    @NotNull(message = "Name field is not provide")
    @NotBlank(message = "Name must not be blank")
    @JsonProperty("name")
    private String  name;

    @NotNull(message = "Description field is not provide")
    @JsonProperty("description")
    private String description;


    private List<Place> places ;


    private User user;

    @NotNull (message = "tags field is not provide")
    @Valid
    @JsonProperty("tags")
    private List<Tag> tags;


    @NotNull (message = "friends field is not provide")
    @Valid
    @JsonProperty("friends")
    private List<Friend> friends;


    @NotNull (message = "isPrivate field is not provide")
    @Pattern(regexp = "^true|false$+",message = "Private must be a boolean")
    @JsonProperty("isPrivate")
    private String isPrivate;


    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @JsonCreator
    public EventMap() {
        this.places = new ArrayList<Place>();
        this.user = null;
        this.tags = null;
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

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setVisibility(String aPrivate) {
        isPrivate = aPrivate;
    }
    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isprivate) {
        this.isPrivate = isprivate;
    }
    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }


}
