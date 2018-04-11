package com.genie3.eventsLocation.exception;

import com.genie3.eventsLocation.entities.Error;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collections;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<javax.validation.ValidationException> {


    public Response toResponse(javax.validation.ValidationException e) {
        final StringBuilder strBuilder = new StringBuilder();
        ArrayList<String> msg = new ArrayList<String>();

        Response.Status status = Response.Status.BAD_REQUEST;
        try{
            for (ConstraintViolation<?> cv : ((ConstraintViolationException) e).getConstraintViolations()) {

                msg.add(cv.getMessage());
            }

        }catch (Exception e1){
            msg.add(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }


        Collections.reverse(msg);
        Error error = new Error(msg);
        return Response
                .status(status.getStatusCode())
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}