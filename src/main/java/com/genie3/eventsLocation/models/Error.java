package com.genie3.eventsLocation.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

public class Error {

    public  ArrayList<String> message;

    @JsonCreator
    public Error(@JsonProperty("message") ArrayList<String> message) {
        this.message = message;
    }


}
