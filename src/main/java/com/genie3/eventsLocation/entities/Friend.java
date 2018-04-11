package com.genie3.eventsLocation.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class Friend {


    private String pseudo;

    public Friend(){
        this.pseudo = "";
    }

    @JsonCreator
    public Friend(@JsonProperty("pseudo") String pseudo){

        this.pseudo = pseudo;
    }


    @NotNull
    @NotEmpty
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String name) {
        this.pseudo = name;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "pseudo='" + pseudo + '\'' +
                '}';
    }
}
