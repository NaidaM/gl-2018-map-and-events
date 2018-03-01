package com.genie3.eventsLocation.ws;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.genie3.eventsLocation.constraints.AttemptAuthUser;
import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.models.Error;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.User;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/auth")

public class AuthResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response get(@AttemptAuthUser User user) {

        if(Dao.getUserDao().authenticate(user.getPseudo(),user.getPassword())){
           String token =  Dao.getUserDao().getToken(user.getPseudo());
            return Response.status(Response.Status.OK).entity(token).build();
        }else {
            ArrayList<String> m = new ArrayList<String>();
            m.add("Authentification failed");
            Error error = new Error(m);
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }

        /*
        *   return Response.status(Response.Status.OK).entity(user).build();
        *    use this to return result with specific Http status and the method return must be
        *    javax.ws.rs.core.Response
        */
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response create(@Valid @ValidPassword User user)  {

        // Penser à mettre les exception
        User user1 = Dao.getUserDao().create(user);
        return Response.status(Response.Status.CREATED).entity(user1).build();
    }

}
